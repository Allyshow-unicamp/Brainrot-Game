package com.brainrots.screen;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.MainUX.MainUX;
import com.brainrots.MainUX.TowerButton;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.engine.LevelManager;


public class GameplayScreen extends GameScreen {

    private LevelManager levelManager;

    private OrthographicCamera main_camera;

    private Label hpLabel;
    private Label coinLabel;
    private Label waveLabel;
    private BitmapFont hudFont;
    private float fontScale;

    private MainUX mainUX;

    public GameplayScreen(SpriteBatch batch, BrainRotGame screenManager,
         ShapeRenderer shapeRenderer, int level, String levelName) {
        super(batch, screenManager, shapeRenderer);

        main_camera = new OrthographicCamera();
        mainUX = new MainUX(1920, 1080, batch);
        levelManager = new LevelManager(resourceManager, screenManager, level, levelName, main_camera, stage, mainUX);

        initializeHud();
    }


    private void update(float delta)
    {
        main_camera.update();
        batch.setProjectionMatrix(main_camera.combined);
        levelManager.update(delta);
        stage.act(delta);
        levelManager.draw(batch,shapeRenderer);

        batch.setProjectionMatrix(stage.getCamera().combined);
        mainUX.draw();
    }

    @Override
    protected void initializeHud() {
        main_camera.setToOrtho(false, 1920, 1080);

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        fontScale = 1.0f;
        hudFont = resourceManager.createFont(Assets.UI_FONT, fontScale);
        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        labelStyle.font = hudFont;
        hpLabel = new Label("HP: " + levelManager.getBaseHealth(), labelStyle);
        coinLabel = new Label("Coins: " + levelManager.getCoins(), labelStyle);
        waveLabel = new Label("Current wave: " + levelManager.getCurrentWave(), labelStyle);

        ImageButton waveControlButton = mainUX.getWaveButton();

        waveControlButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
            if(waveControlButton.isChecked()){
                levelManager.resumeWaveGeneration();
           } else{
                levelManager.pauseWaveGeneration();
           }
        }});

        stage.addActor(waveControlButton);
        stage.addActor(hpLabel);
        stage.addActor(coinLabel);
        stage.addActor(waveLabel);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);

        float baseSize = Math.min(width, height);
        float newScale = (baseSize / 540f) * fontScale;
        hudFont.getData().setScale(newScale);

        hpLabel.setPosition(10, stage.getHeight() - hpLabel.getHeight() - 10);
        coinLabel.setPosition(10, stage.getHeight() - hpLabel.getHeight() - coinLabel.getHeight() - 20);
        waveLabel.setPosition(10, stage.getHeight() - hpLabel.getHeight() - coinLabel.getHeight() - waveLabel.getHeight() - 30);

        mainUX.resize(width, height);

        if (mainUX.getTowerUpgradePanel() != null) {
            mainUX.getTowerUpgradePanel().resizePanel(width, height);
        }
    }

    @Override
    protected void drawScreen(float delta) {
        update(delta);

        hpLabel.setText("HP: " + levelManager.getBaseHealth());
        coinLabel.setText("Coins: " + levelManager.getCoins());
        waveLabel.setText("Current wave: " + levelManager.getCurrentWave());

        mainUX.getWaveButton().setChecked(!levelManager.isWavePaused());

        stage.draw();
    }

    @Override
    public void show() {
        super.show();

        Gdx.input.setInputProcessor(stage);

        mainUX.buildTowerPanel();
        Skin uiSkin = resourceManager.getSkin(Assets.UI_SKIN);
        mainUX.buildUpgradePanel(uiSkin);
        mainUX.getTowerUpgradePanel().setOnCloseCallback(() -> {
            levelManager.clearSelectedTower();
        });

        Iterator<TowerButton> iterator = mainUX.getTowerButtons().iterator();

        while (iterator.hasNext()) {
            TowerButton button = iterator.next();
            button.setClickEvent(() -> {
                levelManager.positionTowerMode(button.getTowerType());
            });
        }

        ScrollPane scrollPane = mainUX.getTowerScrollPane();
        if (scrollPane != null) {
            stage.addActor(scrollPane);
        }

        if (mainUX.getTowerUpgradePanel() != null) {
            stage.addActor(mainUX.getTowerUpgradePanel());
        }

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

}
