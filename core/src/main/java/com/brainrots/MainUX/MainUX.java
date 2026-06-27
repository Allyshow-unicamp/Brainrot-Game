package com.brainrots.MainUX;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.brainrots.MainUX.UpgradeUX.TowerUpgradeMenu;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.tower.TestTower;
import com.brainrots.tower.Tower;


public class MainUX {

    private static final float DESIGN_W = 1920;
    private static final float DESIGN_H = 1080;

    private TowerUpgradeMenu towerUpgradeMenu;
    private Texture towerBackground;
    private Vector<TestTower> towerTypes;
    private Array<TowerButton> towerButtons;
    private Array<Label> priceLabels;
    private ScrollPane towerScrollPane;
    private Table towerTable;
    private ImageButton waveButton;
    private SpriteBatch batch;
    private boolean initialized;
    private float screenWidth;
    private float screenHeight;

    public MainUX(int width, int height, SpriteBatch batch)
    {
        towerUpgradeMenu = new TowerUpgradeMenu();
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

    private void loadTowerTypes()
    {
        towerTypes.add(TestTower.TOWER_TUNG);
        towerTypes.add(TestTower.TOWER_VACA_SATURNITA);
        towerTypes.add(TestTower.TOWER_TRIPPI_TROPPI);
        towerTypes.add(TestTower.TOWER_TRALALERO_TRALALA);
        towerTypes.add(TestTower.TOWER_PATAPIM);
        towerTypes.add(TestTower.TOWER_NUCLEARO_DINOSAURO);
        towerTypes.add(TestTower.TOWER_LIRILI_LARILA);
        towerTypes.add(TestTower.TOWER_CAPPUCCINO);
        towerTypes.add(TestTower.TOWER_CAPPUCCINA);
        towerTypes.add(TestTower.TOWER_BOMBARDILO_CROCODILO);
    }

    public void buildTowerPanel() {
        if (initialized) return;
        initialized = true;

        GameResourceManager rm = GameResourceManager.getGameResourceManager();
        Skin skin = rm.getSkin(Assets.UI_SKIN);

        towerTable = new Table();
        towerTable.top().left();
        towerTable.defaults().pad(4);

        BitmapFont priceFont = new BitmapFont(Gdx.files.internal(Assets.UI_FONT));
        priceFont.getData().setScale(0.75f);
        Label.LabelStyle costStyle = new Label.LabelStyle(priceFont, Color.YELLOW);

        for (int i = 0; i < towerTypes.size(); i++) {
            TestTower type = towerTypes.get(i);

            TowerButton btn = new TowerButton(type, 0, 0);
            towerButtons.add(btn);
            towerTable.add(btn).size(80, 80).padRight(8);

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

        float waveBtnSize = height * 0.07f;
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

    public TowerUpgradeMenu getTowerUpgradeMenu() {
        return towerUpgradeMenu;
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

    public void updateCoins(int newCoinCount) {
    }

    public void clear() {
    }
}
