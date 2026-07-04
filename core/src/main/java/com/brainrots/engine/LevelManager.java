package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.brainrots.tower.BrainrotTower;
import com.brainrots.tower.Projectile;
import com.brainrots.tower.Tower;

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
    private Stage stage;

    private MainUX mainUX;
    private CoinManager coinManager;
    private ProjectileManager projectileManager;
    private TowerPlacementController placementController;

    private Tower selectedTower;
    private Texture rangeCircleTexture;
    private Texture projectileTexture;

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
        this.mainUX = mainUX;
        this.coinManager = new CoinManager(300);
        this.projectileManager = new ProjectileManager();
        this.placementController = new TowerPlacementController(gameScreen, stage);
        this.selectedTower = null;
        this.projectileTexture = resourceManager.getTexture(Assets.PROJECTILE_BLACK);
        createRangeCircleTexture();
        mainUX.setCoinManager(coinManager);
    }

    private void createRangeCircleTexture() {
        int texSize = 256;
        Pixmap pixmap = new Pixmap(texSize, texSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f, 0f, 1f, 0.3f);
        pixmap.fillCircle(texSize / 2, texSize / 2, texSize / 2);
        rangeCircleTexture = new Texture(pixmap);
        pixmap.dispose();
    }

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

    public int getBaseHealth() {
        return base.getHealth();
    }

    public void positionTowerMode(BrainrotTower towerType)
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
        handleTowerClick();

        gameTime += delta;

        for (Tower tower : activeTowers) {
            tower.update(gameTime, activeEnemies, activeTowers, coinManager, projectileManager, projectileTexture);
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

        if (mainUX.isUpgradePanelOpen()) {
            mainUX.getTowerUpgradePanel().refresh();
        }
    }

    public Tower getSelectedTower() {
        return selectedTower;
    }

    private void handleTowerClick() {
        if (!Gdx.input.justTouched()) return;

        Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Actor hitActor = stage.hit(stageCoords.x, stageCoords.y, true);
        if (hitActor != null) return;

        Vector3 clickCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.unproject(clickCoords);

        Tower clickedTower = null;
        for (Tower tower : activeTowers) {
            if (tower.wasClicked(clickCoords.x, clickCoords.y)) {
                clickedTower = tower;
                break;
            }
        }

        if (clickedTower != null) {
            if (clickedTower != selectedTower || !mainUX.isUpgradePanelOpen()) {
                selectedTower = clickedTower;
                mainUX.openUpgradePanel(clickedTower);
            }
        } else {
            selectedTower = null;
            mainUX.closeUpgradePanel();
        }
    }

    public void clearSelectedTower() {
        selectedTower = null;
        mainUX.closeUpgradePanel();
    }

    private void rewardCoins(Enemy enemy)
    {
        coinManager.addCoins(enemy.giveCoinOnDeath());
    }

    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        batch.begin();

        Texture background = map.getBackground();
        batch.draw(background, 0, 0);

        if (selectedTower != null && rangeCircleTexture != null) {
            float range = selectedTower.getRange();
            float cx = selectedTower.getPosition().x;
            float cy = selectedTower.getPosition().y + selectedTower.getTowerHeight() / 2f;
            batch.setColor(0f, 0f, 1f, 0.2f);
            batch.draw(rangeCircleTexture, cx - range, cy - range, range * 2, range * 2);
            batch.setColor(1f, 1f, 1f, 1f);
        }

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
