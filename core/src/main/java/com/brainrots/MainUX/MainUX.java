package com.brainrots.MainUX;

import java.util.Vector;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brainrots.MainUX.UpgradeUX.TowerUpgradeMenu;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.tower.Tower;


/**
 * Classe usada como objetivo de auxiliar LevelManager. 
 * <p>Vai possuir os elementos principais pra uma boa UX</p>
 */
public class MainUX {

    private CoinHUD coinHUD;
    private TowerUpgradeMenu towerUpgradeMenu;
    private Texture towerBackground;
    private Vector<Texture> towerIcons;
    private Array<TowerButton> towerButtons;
    private ImageButton waveButton;
    private SpriteBatch batch;
    private Vector2 UISize;

    public MainUX(int width, int height, SpriteBatch batch)
    {
        towerUpgradeMenu = new TowerUpgradeMenu();
        towerIcons = new Vector<>();

        this.batch = batch;

        UISize = new Vector2(width, height);
        towerButtons = new Array<>();

        Texture coinTexture = GameResourceManager.getGameResourceManager().getTexture(Assets.NORMAL_COIN);
        this.coinHUD = new CoinHUD(coinTexture, 0);
        coinHUD.setPosition(0, UISize.y);

        loadTowerIcons();
        generateTowerHUDBackground();

        GameResourceManager resourceManager = GameResourceManager.getGameResourceManager();

        Texture playButton = resourceManager.getTexture(Assets.PLAY_BUTTON);
        Texture pauseButton = resourceManager.getTexture(Assets.PAUSE_BUTTON);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();

        style.imageUp = new TextureRegionDrawable(pauseButton);
        style.imageChecked = new TextureRegionDrawable(playButton);

        waveButton = new ImageButton(style);

        waveButton.setPosition(UISize.x - pauseButton.getWidth(), 0);
    }

    private void loadTowerIcons()
    {
        GameResourceManager resourceManager = GameResourceManager.getGameResourceManager();

        towerIcons.add(resourceManager.getTexture(Assets.TOWER_TUNG));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_VACA_SATURNITA));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_TRIPPI_TROPPI));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_TRALALERO_TRALALA));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_PATAPIM));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_NUCLEARO_DINOSAURO));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_LIRILI_LARILA));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_CAPPUCCINO));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_CAPPUCCINA));
        towerIcons.add(resourceManager.getTexture(Assets.TOWER_BOMBARDILO_CROCODILO));
    }

    public void resize(float width, float height){
        UISize = new Vector2(width, height);

        int x_paddle = 100;
        int y_paddle = 130;

        for(int i = 0; i < towerButtons.size; i++){
            towerButtons.get(i).setPosition(UISize.x - x_paddle, y_paddle + (80 * i));
        }

        waveButton.setPosition(UISize.x - 128, 0);
    }

    private void generateTowerHUDBackground(){
        Pixmap pixmap = new Pixmap(30, 30, Pixmap.Format.RGBA8888);
        pixmap.setColor(0f / 255f, 0f / 255f, 51f / 255f, 0.5f);
        pixmap.fill();

        towerBackground = new Texture(pixmap);

        pixmap.dispose();
    }

    public ImageButton getWaveButton(){
        return waveButton;
    }

    public void draw(){
        batch.begin();
        batch.draw(towerBackground, 1920 - 165, 0, 165, UISize.y * 2);
        batch.end();
    }

    public Array<TowerButton> getTowerButtons(){
        int x_paddle = 100;
        int y_paddle = 130;

        for(int i = 0; i < towerIcons.size(); i++){
            Texture castleTexture = towerIcons.get(i);

            towerButtons.add(new TowerButton(castleTexture, UISize.x - x_paddle, y_paddle + (80 * i), () -> {
            }));

        }

        return towerButtons;
    }

    public CoinHUD getCoinHUD(){
        return coinHUD;
    }

    public void setTowerUpgradeMenu(TowerUpgradeMenu menu) {
        this.towerUpgradeMenu = menu;
    }

    public void openUpgradeMenu(Tower tower, Stage stage,
        OrthographicCamera gameScreen,Array<Tower> activeTowers) 
    {
        towerUpgradeMenu.openUpgradeMenu(tower, stage,gameScreen,activeTowers);
    }

    public void closeUpgradeMenu()
    {
        towerUpgradeMenu.closeUpgradeMenu();
    }

    public void registerCoinHUD(CoinHUD hud) {
        this.coinHUD = hud;
    }

    public void updateCoins(int newCoinCount) {
        if (coinHUD != null) {
            coinHUD.updateCoins(newCoinCount);
        }
    }

    public void clear() {
        coinHUD = null;
    }

    public int getCurrentCoins() {
        return coinHUD.getCurrentCoins();
    }

}
