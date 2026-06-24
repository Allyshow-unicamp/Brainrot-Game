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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.MainUX.MainUX;
import com.brainrots.MainUX.TowerButton;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.engine.LevelManager;


/**
 * Classe que inicializa uma tela de level.
 */
public class GameplayScreen extends GameScreen {

    private LevelManager levelManager;

    private OrthographicCamera main_camera;

    private Label hpLabel;

    private MainUX mainUX;

    /**
     * Inicializa o jogo.
     */
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

        mainUX.draw();
    }

    @Override
    protected void initializeHud() {
        main_camera.setToOrtho(false, 1920, 1080);

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        BitmapFont font = resourceManager.getFont(Assets.UI_FONT);
        font.getData().setScale(2.0f);
        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        labelStyle.font = font;
        hpLabel = new Label("HP: " + levelManager.getBaseHealth(), labelStyle);

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
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height,true);
        hpLabel.setPosition(10, stage.getHeight() - hpLabel.getHeight() - 10);
       
        mainUX.resize(width, height);
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

        Iterator<TowerButton> iterator = mainUX.getTowerButtons().iterator();

        while (iterator.hasNext()) {
            TowerButton button = iterator.next();
            button.setClickEvent(() -> {
                levelManager.positionTowerMode(button.getTexture());
            });

            stage.addActor(button);
        }

        stage.addActor(mainUX.getCoinHUD());
        resize(1920,1080);
    }

}
