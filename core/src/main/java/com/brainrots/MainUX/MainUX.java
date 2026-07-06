package com.brainrots.MainUX;

import java.util.Vector;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.CoinManager;
import com.brainrots.tower.BrainrotTower;
import com.brainrots.tower.Tower;


public class MainUX {

    private TowerUpgradePanel towerUpgradePanel;
    private TowerInfoModal towerInfoModal;
    private Texture towerBackground;
    private Vector<BrainrotTower> towerTypes;
    private Array<TowerButton> towerButtons;
    private Array<Label> priceLabels;
    private Array<ImageButton> infoButtons;
    private ScrollPane towerScrollPane;
    private Table towerTable;
    private ImageButton waveButton;
    private ImageButton speedButton;
    private ImageButton menuButton;
    private Texture speedTexture;
    private Texture speedActiveTexture;
    private Texture menuTexture;
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
        infoButtons = new Array<>();
        initialized = false;

        generateTowerHUDBackground();

        GameResourceManager rm = GameResourceManager.getGameResourceManager();

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.imageUp = new TextureRegionDrawable(rm.getTexture(Assets.PLAY_BUTTON));
        style.imageChecked = new TextureRegionDrawable(rm.getTexture(Assets.PAUSE_BUTTON));
        waveButton = new ImageButton(style);
        waveButton.setSize(64, 64);

        createSpeedButtonTexture();
        ImageButton.ImageButtonStyle speedStyle = new ImageButton.ImageButtonStyle();
        speedStyle.imageUp = new TextureRegionDrawable(new TextureRegion(speedTexture));
        speedStyle.imageChecked = new TextureRegionDrawable(new TextureRegion(speedActiveTexture));
        speedButton = new ImageButton(speedStyle);
        speedButton.setSize(64, 64);

        createMenuButtonTexture();
        ImageButton.ImageButtonStyle menuStyle = new ImageButton.ImageButtonStyle();
        menuStyle.imageUp = new TextureRegionDrawable(new TextureRegion(menuTexture));
        menuButton = new ImageButton(menuStyle);
        menuButton.setSize(64, 64);
    }

    private void createSpeedButtonTexture() {
        int s = 64;
        speedTexture = createCircleTexture(s, 0, 0.5f, 0);
        speedActiveTexture = createCircleTexture(s, 0, 1f, 0);
    }

    private void createMenuButtonTexture() {
        int s = 64;
        menuTexture = createCircleTexture(s, 0.6f, 0.3f, 0);
    }

    private Texture createCircleTexture(int size, float r, float g, float b) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, 1);
        pixmap.fillCircle(size / 2, size / 2, size / 2 - 2);
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    public void setCoinManager(CoinManager coinManager) {
        this.coinManager = coinManager;
    }

    public void setTowerInfoModal(TowerInfoModal modal) {
        this.towerInfoModal = modal;
    }

    public TowerInfoModal getTowerInfoModal() {
        return towerInfoModal;
    }

    public void buildUpgradePanel(Skin skin) {
        if (towerUpgradePanel != null) return;
        towerUpgradePanel = new TowerUpgradePanel(skin, coinManager);
    }

    public TowerUpgradePanel getTowerUpgradePanel() {
        return towerUpgradePanel;
    }

    public void setUnlockedTowerIds(java.util.List<String> unlockedIds)
    {
        towerTypes.clear();
        for (BrainrotTower tower : BrainrotTower.values()) {
            if (unlockedIds.contains(tower.getTowerId())) {
                towerTypes.add(tower);
            }
        }
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

        Pixmap infoPixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        infoPixmap.setColor(0, 0, 0, 0);
        infoPixmap.fill();
        infoPixmap.setColor(0, 1, 1, 1);
        infoPixmap.drawCircle(10, 10, 9);
        infoPixmap.setColor(0, 1, 1, 1);
        infoPixmap.drawPixel(10, 14);
        infoPixmap.drawPixel(10, 12);
        infoPixmap.drawPixel(10, 10);
        infoPixmap.drawPixel(10, 8);
        infoPixmap.drawPixel(10, 6);
        Texture infoTexture = new Texture(infoPixmap);
        infoPixmap.dispose();
        TextureRegionDrawable infoDrawable = new TextureRegionDrawable(new TextureRegion(infoTexture));

        for (int i = 0; i < towerTypes.size(); i++) {
            BrainrotTower type = towerTypes.get(i);

            TowerButton btn = new TowerButton(type, 0, 0);
            towerButtons.add(btn);
            towerTable.add(btn).size(100, 100).padRight(4);

            Table infoAndCost = new Table();
            Label costLabel = new Label("$" + type.getCost(), costStyle);
            priceLabels.add(costLabel);
            infoAndCost.add(costLabel).left().padBottom(2);
            infoAndCost.row();

            ImageButton infoBtn = new ImageButton(infoDrawable);
            infoBtn.setSize(24, 24);
            infoButtons.add(infoBtn);
            infoAndCost.add(infoBtn).size(24, 24).left();

            towerTable.add(infoAndCost).left().padRight(4);

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
        float gap = 10;
        waveButton.setSize(waveBtnSize, waveBtnSize);
        waveButton.setPosition(width - waveBtnSize - 15, 15);

        speedButton.setSize(waveBtnSize, waveBtnSize);
        speedButton.setPosition(width - 2 * (waveBtnSize + gap) - 15, 15);

        menuButton.setSize(waveBtnSize, waveBtnSize);
        menuButton.setPosition(width - 3 * (waveBtnSize + gap) - 15, 15);
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

    public ImageButton getSpeedButton(){
        return speedButton;
    }

    public ImageButton getMenuButton(){
        return menuButton;
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

    public Array<ImageButton> getInfoButtons() {
        return infoButtons;
    }

    public void updateButtonStates(int coins) {
        for (int i = 0; i < towerButtons.size && i < towerTypes.size(); i++) {
            TowerButton btn = towerButtons.get(i);
            BrainrotTower type = towerTypes.get(i);
            boolean canAfford = coins >= type.getCost();
            btn.setAffordable(canAfford);
        }
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
