package com.brainrots.MainUX;

import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.CoinManager;
import com.brainrots.tower.BrainrotTower;
import com.brainrots.tower.Tower;


public class MainUX {

    private TowerUpgradePanel towerUpgradePanel;
    private Texture towerBackground;
    private Vector<BrainrotTower> towerTypes;
    private Array<TowerButton> towerButtons;
    private Array<Label> priceLabels;
    private ScrollPane towerScrollPane;
    private Table towerTable;
    private ImageButton waveButton;
    private SpriteBatch batch;
    private boolean initialized;
    private float screenWidth;
    private float screenHeight;
    private CoinManager coinManager;

    public MainUX(int width, int height, SpriteBatch batch)
    {
        towerTypes = new Vector<>();

        this.batch = batch;
        towerButtons = new Array<>();
        priceLabels = new Array<>();
        initialized = false;

        loadTowerTypes();
        generateTowerHUDBackground();

        GameResourceManager rm = GameResourceManager.getGameResourceManager();

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.getTexture(Assets.PLAY_BUTTON));
        style.imageChecked = new TextureRegionDrawable(rm.getTexture(Assets.PAUSE_BUTTON));
        waveButton = new ImageButton(style);
        waveButton.setSize(64, 64);
    }

    public void setCoinManager(CoinManager coinManager) {
        this.coinManager = coinManager;
    }

    public void buildUpgradePanel(Skin skin) {
        if (towerUpgradePanel != null) return;
        towerUpgradePanel = new TowerUpgradePanel(skin, coinManager);
    }

    public TowerUpgradePanel getTowerUpgradePanel() {
        return towerUpgradePanel;
    }

    private void loadTowerTypes()
    {
        towerTypes.add(BrainrotTower.TUNG_TUNG);
        towerTypes.add(BrainrotTower.BOMBARDILO_CROCODILO);
        towerTypes.add(BrainrotTower.NUCLEARO_DINOSAURO);
        towerTypes.add(BrainrotTower.CAPPUCCINO_ASSASSINO);
        towerTypes.add(BrainrotTower.BALLERINA_CAPPUCCINA);
    }

    public void buildTowerPanel() {
        if (initialized) return;
        initialized = true;

        GameResourceManager rm = GameResourceManager.getGameResourceManager();
        Skin skin = rm.getSkin(Assets.UI_SKIN);

        towerTable = new Table();
        towerTable.top().left();
        towerTable.defaults().pad(4);

        BitmapFont priceFont = rm.createFont(Assets.UI_FONT, 1.25f);
        Label.LabelStyle costStyle = new Label.LabelStyle(priceFont, Color.YELLOW);

        for (int i = 0; i < towerTypes.size(); i++) {
            BrainrotTower type = towerTypes.get(i);

            TowerButton btn = new TowerButton(type, 0, 0);
            towerButtons.add(btn);
            towerTable.add(btn).size(100, 100).padRight(8);

            Label costLabel = new Label("$" + type.getCost(), costStyle);
            priceLabels.add(costLabel);
            towerTable.add(costLabel).left();

            towerTable.row();
        }

        towerTable.pack();

        towerScrollPane = new ScrollPane(towerTable, skin);
        towerScrollPane.setScrollingDisabled(true, false);
        towerScrollPane.setFadeScrollBars(false);
    }

    public void resize(float width, float height){
        this.screenWidth = width;
        this.screenHeight = height;

        float panelWidth = width * 0.13f;
        float btnSize = height * 0.085f;

        if (towerScrollPane != null) {
            towerScrollPane.setSize(panelWidth, height * 0.85f);
            towerScrollPane.setPosition(width - panelWidth, height * 0.15f);
        }

        if (towerTable != null) {
            for (Actor child : towerTable.getChildren()) {
                if (child instanceof TowerButton) {
                    child.setSize(btnSize, btnSize);
                }
            }
        }

        float waveBtnSize = height * 0.1f;
        waveButton.setSize(waveBtnSize, waveBtnSize);
        waveButton.setPosition(width - waveBtnSize - 15, 15);
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
        if (screenWidth <= 0 || screenHeight <= 0) return;
        float panelWidth = screenWidth * 0.13f;
        float panelY = screenHeight * 0.15f;
        float panelHeight = screenHeight * 0.85f;
        batch.begin();
        batch.draw(towerBackground, screenWidth - panelWidth, panelY, panelWidth, panelHeight);
        batch.end();
    }

    public Array<TowerButton> getTowerButtons(){
        return towerButtons;
    }

    public Array<Label> getPriceLabels() {
        return priceLabels;
    }

    public ScrollPane getTowerScrollPane() {
        return towerScrollPane;
    }

    public void openUpgradePanel(Tower tower) {
        if (towerUpgradePanel != null) {
            towerUpgradePanel.open(tower);
        }
    }

    public void closeUpgradePanel() {
        if (towerUpgradePanel != null) {
            towerUpgradePanel.close();
        }
    }

    public boolean isUpgradePanelOpen() {
        return towerUpgradePanel != null && towerUpgradePanel.isOpen();
    }

    public void updateCoins(int newCoinCount) {
    }

    public void clear() {
    }
}
