package com.brainrots.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brainrots.MainUX.CoinHUD;
import com.brainrots.MainUX.MainUX;
import com.brainrots.MainUX.TowerButton;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.LevelManager;


/**
 * Classe que inicializa uma tela de level.
 */
public class GameplayScreen extends GameScreen {

    private LevelManager levelManager;

    private OrthographicCamera main_camera;

    private Label hpLabel;
    private TowerButton towerButton;
    private CoinHUD coinHUD;

    /**
     * Inicializa o jogo.
     */
    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager,
                BrainRotGame screenManager, ShapeRenderer shapeRenderer, int level, String levelName) {
        super(batch, screenManager, shapeRenderer);
        
        main_camera = new OrthographicCamera();
        levelManager = new LevelManager(resourceManager, screenManager, level, levelName, main_camera,stage);

        initializeHud();

        levelManager.startNextWave();
    }


    private void update(float delta)
    {
        main_camera.update();
        batch.setProjectionMatrix(main_camera.combined);
        levelManager.update(delta);
        stage.act(delta);
        levelManager.draw(batch,shapeRenderer);
    }

    @Override
    protected void initializeHud() {
        main_camera.setToOrtho(false, 1920, 1080);

        Texture playButton = resourceManager.getTexture(Assets.PLAY_BUTTON);
        Texture pauseButton = resourceManager.getTexture(Assets.PAUSE_BUTTON);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();

        style.imageUp = new TextureRegionDrawable(playButton);
        style.imageChecked = new TextureRegionDrawable(pauseButton);

        ImageButton waveControlButton = new ImageButton(style);

        waveControlButton.setPosition(0, 0);

        waveControlButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
            if(waveControlButton.isChecked()){
                levelManager.pauseWaveGeneration();
           } else{
                levelManager.resumeWaveGeneration();
           }
        }});

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        BitmapFont font = resourceManager.getFont(Assets.UI_FONT);
        font.getData().setScale(2.0f);
        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        labelStyle.font = font;
        hpLabel = new Label("HP: " + levelManager.getBaseHealth(), labelStyle);


        stage.addActor(waveControlButton);
        stage.addActor(hpLabel);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height,true);
        hpLabel.setPosition(10, stage.getHeight() - hpLabel.getHeight() - 10);
        if (towerButton != null) {
            float margem = 20f;
            float botaoX = stage.getWidth() - towerButton.getWidth() - margem;
            float botaoY = stage.getHeight() - towerButton.getHeight() - margem;
            towerButton.setPosition(botaoX, botaoY);
        }
    }

    @Override
    protected void drawScreen(float delta) {
        update(delta);

        hpLabel.setText("HP: " + levelManager.getBaseHealth());

        stage.draw();
    }

    @Override
    public void show() {
        super.show();

        Gdx.input.setInputProcessor(stage);
        Texture castleTexture = MainUX.getTowerIcon().get(0);

        this.towerButton = new TowerButton(castleTexture, 0, 0, () -> {
            levelManager.positionTowerMode(castleTexture);
        });

        Texture coiTexture = MainUX.getCoinTexture();
        this.coinHUD = new CoinHUD(coiTexture, 0);
        coinHUD.setPosition(1000, 680);
        MainUX.getMainUX().registerCoinHUD(coinHUD);

        stage.addActor(coinHUD);
        stage.addActor(towerButton);
        resize(1920,1080);
    }

}
