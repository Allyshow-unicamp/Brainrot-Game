package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.brainrots.MainUX.MainUX;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.tower.ConcreteTower;
import com.brainrots.tower.Projectile;
import com.brainrots.tower.TargetType;
import com.brainrots.tower.Tower;

public class LevelManager {

    private final int maxHealth = 100;

    private float gameTime;

    private int level;
    private int coins;
    private Map map;
    private Array<Enemy> activeEnemies;
    private Array<Projectile> activeProjectiles;
    private Array<Tower> activeTowers;
    private Base base;
    private WaveManager waveManager;
    private GameResourceManager resourceManager;
    private Texture towerGhostTexture;
    private BrainRotGame screenManager;
    private OrthographicCamera gameScreen;
    private boolean isIconSelected;
    private boolean isUpgradeMenuOpen;
    private Stage stage;
    private boolean wasTowerUpgraded;

    private boolean running;

    public LevelManager(GameResourceManager resourceManager, BrainRotGame screenManager, 
        int level, String mapName, OrthographicCamera gameScreen, Stage stage){
        this.gameTime = 0;
        this.gameScreen = gameScreen;
        this.coins = 0;
        this.level = level;
        this.resourceManager = resourceManager;
        this.screenManager = screenManager;
        map = new Map(mapName, resourceManager);
        base = new Base(maxHealth, map.getLastPosition());
        activeEnemies = new Array<>();
        activeProjectiles = new Array<>();
        activeTowers = new Array<>();
        waveManager = new WaveManager(mapName, map.getPath());
        this.stage = stage;
        this.isUpgradeMenuOpen = false;
    }

    /**
     * Retorna as coordenadas do caminho dos inimigos.
     * @return Array de Vector2 contendo as coordenadas
     */
    public Array<Vector2> getPath() {
        return map.getPath();
    }

    public void pauseWaveGeneration(){
        waveManager.pauseWaveGeneration();
    }

    public void resumeWaveGeneration(){
        waveManager.resumeWaveGeneration();
    }

    /**
     * Retorna os inimigos ativos no mapa.
     * @return Array de Enemy com os inimigos ativos.
     */
    public Array<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    /**
     * Retorna a vida restante da base.
     * @return Int representando a vida da base.
     */
    public int getBaseHealth() {
        return base.getHealth();
    }

    public void positionTowerMode(Texture towerGhostTexture)
    {
        this.towerGhostTexture = towerGhostTexture;
        this.isIconSelected = true;
    }

    public void startNextWave() {
        waveManager.startNextWave();
    }

    public Enemy getClosestEnemy(Tower tower) {
        float minDist = Float.POSITIVE_INFINITY;
        Enemy minEnemy = null;

        float range = tower.getRange();

        for (Enemy enemy : activeEnemies) {
            float dist = enemy.getPosition().dst(tower.getPosition());

            if(dist > range){
                continue;
            }

            if (dist < minDist) {
                minDist = dist;
                minEnemy = enemy;
            }
        }
        return minEnemy;
    }

    /**
     * Busca inimigos no range de todas as torres.
     */
    public void towerSearch(){

        for (Tower tower : activeTowers) {
            tower.setTarget(getClosestEnemy(tower));
        }
    }

    /**
     * Loop Principal do jogo.
     */
    public void update(float delta) {
        
        towerSearch();

        gameTime += delta;

        float lastAttackTime;
        float attackCooldown;

        for (Tower tower : activeTowers) {

            towerClick(tower);
            lastAttackTime = tower.getLastAttackTime();
            attackCooldown = tower.getAttackCooldown();

            if(gameTime - lastAttackTime >= attackCooldown){
                if(tower.hasTarget()){
                    int damage = tower.getDamage();

                    Enemy target = tower.getTarget();

                    Texture texture = resourceManager.getTexture(Assets.PROJECTILE_BLACK);
                    spawnProjectile(tower.createProjectile(texture, target));

                    tower.attack(gameTime);
                }
            }

        }

        ArrayIterator<Projectile> projIterator = activeProjectiles.iterator();

        while (projIterator.hasNext()) {

            Projectile projectile = projIterator.next();

            projectile.update(delta);

            if(projectile.reachedTarget() || !activeEnemies.contains(projectile.getTarget(), true) ||
             projectile.getPosition().dst(projectile.getParent().getPosition()) > projectile.getParent().getRange()){
                activeProjectiles.removeValue(projectile, false);
             }
        }

        waveManager.update(delta, activeEnemies);


        ArrayIterator<Enemy> enemyIterator = activeEnemies.iterator();

        while (enemyIterator.hasNext()) {
            
            Enemy enemy = enemyIterator.next();

            enemy.update(delta);

            if(enemy.isDead()){
                rewardCoins(enemy);
                activeEnemies.removeValue(enemy, false);
            }

            if (enemy.getPosition().dst(base.getPosition()) < 5.0f) {
                base.takeDamage(enemy.getHp());
                if (!base.hasHealth()) {
                    screenManager.endGame(level, false, 0);
                }

            }
        }

        boolean victory = won(waveManager.getCurrentWave(), waveManager.getMaxWaves());

        if (victory) {
            screenManager.endGame(level, victory, levelStars(base.getHealth(), maxHealth));
        }
        
        checkPositionTower();
    }

    /**
     * Funcao para caso o player clique numa torre pra dar upgrade
     */
    private void towerClick(Tower tower)
    {
        if (Gdx.input.justTouched())
        {

            Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            Actor hitActor = stage.hit(stageCoords.x, stageCoords.y, true);

            if (hitActor != null) {
                return; 
            }

            Vector3 clickCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.unproject(clickCoords);
            if(tower.wasClicked(clickCoords.x,clickCoords.y) && !isUpgradeMenuOpen)
            {
                MainUX.openUpgradeMenu(tower,stage,gameScreen,activeTowers);
                isUpgradeMenuOpen = true;
            }
            else
            {  
                isUpgradeMenuOpen = false;
                MainUX.closeUpgradeMenu();
            }
        }
    }

    /**
     * Funcao que define a posicao da Torre qnd o player clica nela
     */
    private void checkPositionTower()
    {
        if (isIconSelected && Gdx.input.justTouched()) {


            Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            Actor hitActor = stage.hit(stageCoords.x, stageCoords.y, true);

            if (hitActor != null) {
                return; 
            }


            Vector3 mapClick = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            gameScreen.unproject(mapClick);     // Transforma clique em coordenadas no mapa.

            Vector2 transformedCoord = new Vector2(mapClick.x, mapClick.y);

            for(Polygon barrier : map.getBarrier()){
                if(barrier.contains(transformedCoord)){
                    return;
                }
            }

            ConcreteTower concreteTower = new ConcreteTower(towerGhostTexture,
                transformedCoord, 25, 2000, 250, 0.5f,
                150, 150, TargetType.SINGLE,activeTowers.size);

            activeTowers.add(concreteTower);

            isIconSelected = false;
            towerGhostTexture = null;
        }
    }

    private void renderGhostTower(SpriteBatch batch)
    {
        if (isIconSelected && towerGhostTexture != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.unproject(mousePosition);

            for(Polygon barrier : map.getBarrier()){
            
                if(barrier.contains(mousePosition.x, mousePosition.y)){
                    batch.setColor(1, 0, 0, 0.4f);
                    break;
                } else {
                    batch.setColor(1, 1, 1, 0.4f);
                }

            }
            
            float width = 150f;
            float height = 150f;

            batch.draw(towerGhostTexture, mousePosition.x - width/2, mousePosition.y,width, height);

            batch.setColor(1, 1, 1, 1.0f);
        }
    }


    /**
     * Funcao para fornecer moedas na morte do inimigo
     * <p>Fornece e starta a animacao de moedas</p>
     * @param enemy inimigo que acabou de morrer, deve passar por um if verificando isDead()
     */
    private void rewardCoins(Enemy enemy)
    {
        this.coins += enemy.giveCoinOnDeath();
        MainUX.updateCoins(this.coins);
    }

    public void spawnEnemy(Enemy enemy) {
        activeEnemies.add(enemy);
    }

    public void spawnProjectile(Projectile projectile) {
        activeProjectiles.add(projectile);
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();

        Texture background = map.getBackground();
        batch.draw(background, 0, 0);

        for(Enemy enemy : activeEnemies){
            enemy.draw(batch);
        }

        for(Tower tower : activeTowers) {
            tower.draw(batch);
        }

        renderGhostTower(batch);

        for(Projectile projectile: activeProjectiles) {
            projectile.draw(batch);
        }
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for(Enemy enemy : activeEnemies)
        {
            enemy.drawHealthBar(shapeRenderer);
        }
        shapeRenderer.end();
    }


    private boolean won(int currentWave, int maxWaves) {
        return base.getHealth() > 0 && currentWave == maxWaves && activeEnemies.size == 0;
    }

    private int levelStars(int hp, int maxHp) {
        float ratio = hp/maxHp;
        if (ratio < 0.25)
            return 0;
        if (ratio < 0.5)
            return 1;
        if (ratio < 0.75)
            return 2;
        return 3;
    }
}
