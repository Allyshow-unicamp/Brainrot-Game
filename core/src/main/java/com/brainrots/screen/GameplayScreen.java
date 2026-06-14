package com.brainrots.screen;

import org.w3c.dom.Text;

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
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.CoinHUD;
import com.brainrots.engine.LevelManager;


public class GameplayScreen extends GameScreen {

    private OrthographicCamera main_camera;
    private LevelManager levelManager;
    private Label hpLabel;
    private TowerButton towerButton;
    private CoinHUD coinHUD;

    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager,
                BrainRotGame screenManager, ShapeRenderer shapeRenderer, int level, String levelName) {
        super(batch, resourceManager, screenManager, shapeRenderer);

        main_camera = new OrthographicCamera();
        main_camera.setToOrtho(false, 1920, 1080);

        levelManager = new LevelManager(resourceManager, screenManager, level, levelName, main_camera);
        levelManager.startNextWave();

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        BitmapFont font = resourceManager.getFont(Assets.UI_FONT);
        font.getData().setScale(2.0f);
        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        labelStyle.font = font;
        hpLabel = new Label("HP: " + levelManager.getBaseHealth(), labelStyle);

        stage.addActor(hpLabel);


        Texture playButton = resourceManager.getTexture(Assets.PLAY_BUTTON);
        Texture pauseButton = resourceManager.getTexture(Assets.PAUSE_BUTTON);

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();

        style.imageUp = new TextureRegionDrawable(playButton);
        style.imageChecked = new TextureRegionDrawable(pauseButton);

        ImageButton waveControlButton = new ImageButton(style);

        waveControlButton.setPosition(0, 0);

        stage.addActor(waveControlButton);

        waveControlButton.addListener(new ChangeListener() {
           @Override
           public void changed(ChangeEvent event, Actor actor) {
            if(waveControlButton.isChecked()){
                levelManager.pauseWaveGeneration();
           } else{
                levelManager.resumeWaveGeneration();
           }
        }

        });
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

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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

    private void update(float delta)
    {
        main_camera.update();
        batch.setProjectionMatrix(main_camera.combined);
        levelManager.update(delta);
        levelManager.draw(batch,shapeRenderer);
    }

}
