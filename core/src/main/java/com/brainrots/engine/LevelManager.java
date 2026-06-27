package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.brainrots.tower.TestTower;
import com.brainrots.tower.Tower;

public class LevelManager {

    private final int maxHealth = 100;
    private static final int STARTING_COINS = 300;
    private static final float DRAG_THRESHOLD = 20f;
    private static final int TOWER_SIZE = 150;

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
    private TestTower selectedTowerType;
    private boolean wasMousePressed;
    private boolean isMousePressed;
    private Vector2 dragStartScreenPos;
    private boolean isDragging;

    private MainUX mainUX;

    public LevelManager(GameResourceManager resourceManager, BrainRotGame screenManager,
        int level, String mapName, OrthographicCamera gameScreen, Stage stage, MainUX mainUX){
        this.gameTime = 0;
        this.gameScreen = gameScreen;
        this.coins = STARTING_COINS;
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
        this.mainUX = mainUX;
        this.wasMousePressed = false;
        this.isMousePressed = false;
        this.dragStartScreenPos = new Vector2();
        this.isDragging = false;
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

    public boolean isWavePaused() {
        return waveManager.isPausedWave();
    }

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

    public void positionTowerMode(TestTower towerType)
    {
        this.selectedTowerType = towerType;
        this.towerGhostTexture = resourceManager.getTexture(towerType.getTexture());
        this.isIconSelected = true;
        this.isDragging = false;
        this.dragStartScreenPos.set(Gdx.input.getX(), Gdx.input.getY());
    }

    public int getCoins() {
        return coins;
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
            int stars = levelStars(base.getHealth(), maxHealth);
            screenManager.endGame(level, victory, stars);
        }

        wasMousePressed = isMousePressed;
        boolean leftDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightDown = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        isMousePressed = leftDown || rightDown;

        if (isIconSelected && isMousePressed) {
            float dx = Gdx.input.getX() - dragStartScreenPos.x;
            float dy = Gdx.input.getY() - dragStartScreenPos.y;
            if (dx * dx + dy * dy > DRAG_THRESHOLD * DRAG_THRESHOLD) {
                isDragging = true;
            }
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
                mainUX.openUpgradeMenu(tower,stage,gameScreen,activeTowers);
                isUpgradeMenuOpen = true;
            }
            else
            {
                isUpgradeMenuOpen = false;
                mainUX.closeUpgradeMenu();
            }
        }
    }

    private void placeTowerAtPosition(Vector2 worldPosition) {
        if (selectedTowerType == null) return;

        if (coins < selectedTowerType.getCost()) return;

        for(Polygon barrier : map.getBarrier()){
            if(barrier.contains(worldPosition)){
                return;
            }
        }

        coins -= selectedTowerType.getCost();
        mainUX.updateCoins(coins);

        ConcreteTower concreteTower = new ConcreteTower(towerGhostTexture,
            worldPosition,
            selectedTowerType.getDamage(),
            selectedTowerType.getAttackSpeed(),
            selectedTowerType.getRange(),
            selectedTowerType.getAttackCooldown(),
            TOWER_SIZE, TOWER_SIZE,
            selectedTowerType.getTargetType(),
            activeTowers.size);

        activeTowers.add(concreteTower);

        isIconSelected = false;
        towerGhostTexture = null;
        selectedTowerType = null;
        isDragging = false;
    }

    private boolean isCursorOverUI() {
        Vector2 stageCoords = stage.screenToStageCoordinates(
            new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Actor hitActor = stage.hit(stageCoords.x, stageCoords.y, true);
        return hitActor != null;
    }

    private void checkPositionTower()
    {
        if (!isIconSelected) return;

        boolean leftClick = Gdx.input.justTouched() &&
            Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightClick = Gdx.input.justTouched() &&
            Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        boolean dragEnd = isDragging && wasMousePressed && !isMousePressed;

        if (!leftClick && !rightClick && !dragEnd) return;

        if (isCursorOverUI()) return;

        Vector3 currentMouseWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.unproject(currentMouseWorld);
        Vector2 worldPos = new Vector2(currentMouseWorld.x, currentMouseWorld.y);

        placeTowerAtPosition(worldPos);
    }

    private void renderGhostTower(SpriteBatch batch)
    {
        if (isIconSelected && towerGhostTexture != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.unproject(mousePosition);

            boolean canAfford = selectedTowerType != null && coins >= selectedTowerType.getCost();
            boolean onBarrier = false;

            for(Polygon barrier : map.getBarrier()){
                if(barrier.contains(mousePosition.x, mousePosition.y)){
                    onBarrier = true;
                    break;
                }
            }

            if (onBarrier || !canAfford) {
                batch.setColor(1, 0, 0, 0.5f);
            } else {
                batch.setColor(1, 1, 1, 0.4f);
            }

            batch.draw(towerGhostTexture, mousePosition.x - TOWER_SIZE/2,
                mousePosition.y, TOWER_SIZE, TOWER_SIZE);

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
        mainUX.updateCoins(this.coins);
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
        float ratio = (float) hp / maxHp;
        if (ratio < 0.25f)
            return 0;
        if (ratio < 0.5f)
            return 1;
        if (ratio < 0.75f)
            return 2;
        return 3;
    }
}
