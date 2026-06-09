package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.screen.MainUX;
import com.brainrots.screen.ScreenManager;
import com.brainrots.screen.ScreenManager.ScreenType;
import com.brainrots.tower.ConcreteTower;
import com.brainrots.tower.Projectile;
import com.brainrots.tower.Tower;
import com.brainrots.tower.TargetType;

public class LevelManager {

    private final int maxHealth = 100;
    private final int maxWaves = 50;

    private int coins;
    private Map map;
    private Array<Enemy> activeEnemies;
    private Array<Projectile> activeProjectiles;
    private Array<Tower> activeTowers;
    private Base base;
    private WaveManager waveManager;
    private GameResourceManager resourceManager;
    private Texture towerGhostTexture;
    private ScreenManager screenManager;
    private OrthographicCamera gameScreen;
    private boolean isIconSelected;


    public Array<Vector2> getPath() {
        return map.getPath();
    }

    public Array<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    public int getBaseHealth() {
        return base.getHealth();
    }

    public LevelManager(GameResourceManager resourceManager,
        ScreenManager screenManager, String mapName, OrthographicCamera gameScreen){
        this.gameScreen = gameScreen;
        this.coins = 0;
        this.resourceManager = resourceManager;
        this.screenManager = screenManager;
        map = new Map(mapName, resourceManager);
        base = new Base(maxHealth, map.getLastPosition());
        activeEnemies = new Array<>();
        activeProjectiles = new Array<>();
        activeTowers = new Array<>();
        waveManager = new WaveManager(mapName, map.getPath());
    }

    public void positionTowerMode(Texture towerGhostTexture)
    {
        this.towerGhostTexture = towerGhostTexture;
        this.isIconSelected = true;
    }

    public void startNextWave() {
        waveManager.startNextWave();
    }

    public Enemy closestEnemy(Vector2 position) {
        float minDst = Float.POSITIVE_INFINITY;
        Enemy minEnemy = null;
        for (Enemy enemy : activeEnemies) {
            float dst = enemy.getPosition().dst(position);
            if (dst < minDst) {
                minDst = dst;
                minEnemy = enemy;
            }
        }
        return minEnemy;
    }

    public void update(float delta) {
        for (Tower tower : activeTowers) {
            tower.update(delta);
        }

        Array<Projectile> projectilesToBeRemoved = new Array<>();
        for (Projectile projectile : activeProjectiles) {
            projectile.update(delta);

            if (projectile.reachedTarget())
                projectilesToBeRemoved.add(projectile);

            // inimigo que já morreu
            if (!activeEnemies.contains(projectile.getTarget(), true))
                projectilesToBeRemoved.add(projectile);

            if (projectile.getPosition().dst(projectile.getParent().getPosition()) > projectile.getParent().getRange())
                projectilesToBeRemoved.add(projectile);
        }
        for (Projectile projectile : projectilesToBeRemoved) {
            activeProjectiles.removeValue(projectile, true);
        }

        waveManager.update(delta, this, resourceManager);

        Array<Enemy> enemiesToBeRemoved = new Array<>();
        for(Enemy enemy : activeEnemies){
            enemy.update(delta);

            if (enemy.getPosition().dst(base.getPosition()) < 5.0f) {
                boolean alive = base.takeDamage(enemy.getHp());
                if (!alive) {
                    screenManager.switchScreen(ScreenType.MAIN_MENU);
                }
                enemiesToBeRemoved.add(enemy);
            }

            if (enemy.isDead())
                enemiesToBeRemoved.add(enemy);
        }
        for (Enemy enemy : enemiesToBeRemoved){
            activeEnemies.removeValue(enemy, true);
            rewardCoins(enemy);
        }
        checkPositionTower();
    }

    /**
     * Funcao que define a posicao da Torre qnd o player clica nela
     */
    private void checkPositionTower()
    {
        if (isIconSelected && Gdx.input.justTouched()) {
            Vector3 mapClick = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.unproject(mapClick);

            float xFinal = mapClick.x ;
            float yFinal = mapClick.y ;
             ConcreteTower concreteTower = new ConcreteTower(towerGhostTexture,
                new Vector2(xFinal, yFinal), 25, 2000, 250, 0.5f,
                150, 150, TargetType.SINGLE, this, resourceManager);
             buyAndPlaceTower(concreteTower);
            isIconSelected = false;
            towerGhostTexture = null;
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

    public void buyAndPlaceTower(Tower tower) {
        activeTowers.add(tower);
    }

    private void renderGhostTower(SpriteBatch batch)
    {
        if (isIconSelected && towerGhostTexture != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.unproject(mousePosition);

            batch.setColor(1, 1, 1, 0.4f);
            float width = 150f;
            float height = 150f;

            batch.draw(towerGhostTexture, mousePosition.x - width/2, mousePosition.y -height/2 ,width,height);

            batch.setColor(1, 1, 1, 1.0f);
        }
    }
}
