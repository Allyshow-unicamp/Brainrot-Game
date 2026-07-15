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
import com.brainrots.MainUX.TowerUpgradePanel;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameBalance;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.mode.GameMode;
import com.brainrots.mode.GameOutcome;
import com.brainrots.mode.StandardMode;
import com.brainrots.synergy.ElementalSynergy;
import com.brainrots.synergy.SynergyBoost;
import com.brainrots.synergy.SynergyGroup;
import com.brainrots.synergy.SynergyManager;
import com.brainrots.tower.BrainrotTower;
import com.brainrots.tower.Tower;
import com.brainrots.tower.TowerCoin;
import com.brainrots.wave.WaveManager;

/**
 * Orquestrador central do game loop (Facade). Coordena mapa, base, ondas,
 * sinergias, torres, projéteis, moedas e input do jogador a cada frame.
 */
public class LevelManager {

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
    private float speedMultiplier;

    private SynergyManager synergyManager;
    private GameMode gameMode;

    public LevelManager(GameResourceManager resourceManager, BrainRotGame screenManager,
        int level, String mapName, OrthographicCamera gameScreen, Stage stage, MainUX mainUX){
        this.gameTime = 0;
        this.gameScreen = gameScreen;
        this.level = level;
        this.resourceManager = resourceManager;
        this.screenManager = screenManager;
        map = new Map(mapName, resourceManager);
        base = new Base(GameBalance.BASE_MAX_HEALTH, map.getLastPosition());
        activeEnemies = new Array<>();
        activeTowers = new Array<>();
        waveManager = new WaveManager(mapName, map.getPath());
        this.stage = stage;
        this.mainUX = mainUX;
        this.coinManager = new CoinManager(GameBalance.STARTING_COINS);
        this.projectileManager = new ProjectileManager();
        this.placementController = new TowerPlacementController(gameScreen, stage);
        this.selectedTower = null;
        this.speedMultiplier = GameBalance.DEFAULT_SPEED_MULTIPLIER;
        createRangeCircleTexture();
        mainUX.setCoinManager(coinManager);

        // Inicializa sinergias com regras padrão
        this.synergyManager = new SynergyManager();
        synergyManager.addRule(new ElementalSynergy(SynergyGroup.FIRE, GameBalance.FIRE_SYNERGY_THRESHOLD, new SynergyBoost(GameBalance.FIRE_SYNERGY_DAMAGE_MULT)));
        synergyManager.addRule(new ElementalSynergy(SynergyGroup.ICE, GameBalance.ICE_SYNERGY_THRESHOLD, new SynergyBoost(GameBalance.ICE_SYNERGY_DAMAGE_MULT)));
        synergyManager.addRule(new ElementalSynergy(SynergyGroup.PHYSICAL, GameBalance.PHYSICAL_SYNERGY_THRESHOLD, new SynergyBoost(GameBalance.PHYSICAL_SYNERGY_DAMAGE_MULT)));
        synergyManager.addRule(new ElementalSynergy(SynergyGroup.SUPPORT, GameBalance.SUPPORT_SYNERGY_THRESHOLD, new SynergyBoost(GameBalance.SUPPORT_SYNERGY_DAMAGE_MULT, GameBalance.SUPPORT_SYNERGY_RANGE_MULT, GameBalance.SUPPORT_SYNERGY_COOLDOWN_MULT)));

        // Modo padrão
        this.gameMode = new StandardMode();
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

    public int getMaxWaves() {
        return waveManager.getMaxWaves();
    }

    public void startNextWave() {
        waveManager.startNextWave();
    }

    public void setSpeedMultiplier(float multiplier) {
        this.speedMultiplier = multiplier;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void update(float delta) {
        delta *= speedMultiplier;
        handleTowerClick();

        gameTime += delta;

        // Atualiza torres (pula TowerCoin se pausado)
        for (Tower tower : activeTowers) {
            if (tower instanceof TowerCoin && waveManager.isPausedWave()) {
                continue;
            }
            Texture projectileTexture;

            if(tower.getTowerType() == BrainrotTower.TUNG_TUNG){
                projectileTexture = resourceManager.getTexture(Assets.PROJECTILE_WATER);
            } else if(tower.getTowerType() == BrainrotTower.BOMBARDILO_CROCODILO){
                projectileTexture = resourceManager.getTexture(Assets.PROJECTILE_FIRE);
            } else {
                projectileTexture = resourceManager.getTexture(Assets.PROJECTILE_BLACK);
            }
            tower.update(gameTime, activeEnemies, activeTowers, coinManager, projectileManager, projectileTexture);
        }

        // Reavalia sinergias
        synergyManager.evaluate(activeTowers);

        projectileManager.update(delta, activeEnemies);

        waveManager.update(delta, activeEnemies);

        // Processa inimigos
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
                activeEnemies.removeValue(enemy, false);
                if (!base.hasHealth()) {
                    screenManager.endGame(level, false, 0);
                }
            }
        }

        // Avalia vitória/derrota via GameMode
        GameOutcome outcome = gameMode.evaluate(this, base, waveManager, activeEnemies);

        if (outcome == GameOutcome.VICTORY) {
            int stars = levelStars(base.getHealth(), base.getMaxHealth());
            screenManager.endGame(level, true, stars);
        } else if (outcome == GameOutcome.DEFEAT) {
            screenManager.endGame(level, false, 0);
        }

        placementController.update(map.getBarrier(), map.getPath(), activeTowers, coinManager, mainUX);

        if (mainUX.isUpgradePanelOpen()) {
            upgradePanelHandler();
        }
    }

    private void upgradePanelHandler()
    {
        TowerUpgradePanel panel = mainUX.getTowerUpgradePanel();
        if (panel.isSellRequested())
        {
            Tower towerToSell = panel.getSelectedTower();
            activeTowers.removeValue(towerToSell, true);

            panel.clearSellRequest();
            clearSelectedTower();
        }
        else
        {
            panel.refresh();
        }
    }

    public Tower getSelectedTower() {
        return selectedTower;
    }

    /* Só seleciona torre se o clique não foi em ator UI (evita conflito stage/game) */
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

    public void setGameMode(GameMode mode) {
        this.gameMode = mode;
        mode.onEnter(this);
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public String getGameModeSuffix() {
        return gameMode.getHudSuffix();
    }

    public SynergyManager getSynergyManager() {
        return synergyManager;
    }
    /** Calcula estrelas: <33%=1, <66%=2, >=66%=3. */
    private int levelStars(int hp, int maxHp) {
        float ratio = (float) hp / maxHp;
        if (ratio < GameBalance.STAR_THRESHOLD_1) return 1;
        if (ratio < GameBalance.STAR_THRESHOLD_2) return 2;
        return 3;
    }
}
