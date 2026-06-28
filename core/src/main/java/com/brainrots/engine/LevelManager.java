package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
import com.brainrots.tower.Projectile;
import com.brainrots.tower.TargetType;
import com.brainrots.tower.TestTower;
import com.brainrots.tower.Tower;
import com.brainrots.tower.TowerTargetingSystem;

public class LevelManager {

    private final int maxHealth = 100;

    private float gameTime;

    private int level;
    private Map map;
    private Array<Enemy> activeEnemies;
    private Array<Tower> activeTowers;
    private Base base;
    private WaveManager waveManager;
    private GameResourceManager resourceManager;
    private BrainRotGame screenManager;
    private OrthographicCamera gameScreen;
    private boolean isUpgradeMenuOpen;
    private Stage stage;

    private MainUX mainUX;
    private CoinManager coinManager;
    private TowerTargetingSystem targetingSystem;
    private ProjectileManager projectileManager;
    private TowerPlacementController placementController;

    public LevelManager(GameResourceManager resourceManager, BrainRotGame screenManager,
        int level, String mapName, OrthographicCamera gameScreen, Stage stage, MainUX mainUX){
        this.gameTime = 0;
        this.gameScreen = gameScreen;
        this.level = level;
        this.resourceManager = resourceManager;
        this.screenManager = screenManager;
        map = new Map(mapName, resourceManager);
        base = new Base(maxHealth, map.getLastPosition());
        activeEnemies = new Array<>();
        activeTowers = new Array<>();
        waveManager = new WaveManager(mapName, map.getPath());
        this.stage = stage;
        this.isUpgradeMenuOpen = false;
        this.mainUX = mainUX;
        this.coinManager = new CoinManager(300);
        this.targetingSystem = new TowerTargetingSystem();
        this.projectileManager = new ProjectileManager();
        this.placementController = new TowerPlacementController(gameScreen, stage);
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
        Texture ghostTexture = resourceManager.getTexture(towerType.getTexture());
        placementController.selectTower(towerType, ghostTexture);
    }

    public int getCoins() {
        return coinManager.getCoins();
    }

    public int getCurrentWave() {
        return waveManager.getCurrentWave();
    }

    public void startNextWave() {
        waveManager.startNextWave();
    }

    public void update(float delta) {
        targetingSystem.searchAll(activeTowers, activeEnemies);

        gameTime += delta;

        for (Tower tower : activeTowers) {
            towerClick(tower);

            float lastAttackTime = tower.getLastAttackTime();
            float attackCooldown = tower.getAttackCooldown();

            if (gameTime - lastAttackTime >= attackCooldown && tower.hasTarget()) {
                Texture texture = resourceManager.getTexture(Assets.PROJECTILE_BLACK);

                if (tower.getTargetType() == TargetType.MULTIPLE) {
                    Array<Enemy> targets = targetingSystem.findTargets(tower, activeEnemies);
                    for (Enemy t : targets) {
                        projectileManager.spawn(tower.createProjectile(texture, t));
                    }
                } else {
                    projectileManager.spawn(tower.createProjectile(texture, tower.getTarget()));
                }

                tower.attack(gameTime);
            }
        }

        projectileManager.update(delta, activeEnemies);

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

        placementController.update(map.getBarrier(), activeTowers, coinManager, mainUX);
    }

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

    private void rewardCoins(Enemy enemy)
    {
        coinManager.addCoins(enemy.giveCoinOnDeath());
        mainUX.updateCoins(coinManager.getCoins());
    }

    public void spawnEnemy(Enemy enemy) {
        activeEnemies.add(enemy);
    }

    public void spawnProjectile(Projectile projectile) {
        projectileManager.spawn(projectile);
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

        placementController.renderGhost(batch);
        projectileManager.draw(batch);

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
