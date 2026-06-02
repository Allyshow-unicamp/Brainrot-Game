package com.brainrots.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
<<<<<<< Updated upstream
=======
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
>>>>>>> Stashed changes
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.LevelLoader;
import com.brainrots.engine.LevelManager;
import com.brainrots.engine.WorldRenderer;
import com.brainrots.screen.ScreenManager.ScreenType;

import java.util.Vector;

public class GameplayScreen extends GameScreen {

<<<<<<< Updated upstream
    private static final int VIEWPORT_WIDTH = 1920;
    private static final int VIEWPORT_HEIGHT = 1080;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final WorldRenderer worldRenderer = new WorldRenderer();
    private final LevelManager levelManager;
    private final Label hpLabel;
=======
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera; //importtante pra n zikar o jogo, confia
    private LevelManager levelManager;
    private Label hpLabel;
    private TowerButton towerButton;
>>>>>>> Stashed changes

    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager, 
                ScreenManager screenManager, ShapeRenderer shapeRenderer) {
        super(batch, resourceManager, screenManager,shapeRenderer);

<<<<<<< Updated upstream
        Runnable runOnEnd = () -> screenManager.switchScreen(ScreenType.MAIN_MENU);
=======
        camera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        uiCamera.setToOrtho(false,1920,1080);
        levelManager = new LevelManager(resourceManager, screenManager, "67Hills",camera);
        String enemy = "Enemies/temp.png";
        levelManager.startNextWave(new ArrayDeque<>(List.of(enemy, enemy, enemy, enemy)));
>>>>>>> Stashed changes

        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        levelManager = new LevelManager(resourceManager, Assets.MAP_67_HILLS_ID, runOnEnd);
        LevelLoader.loadDefaultLevel(levelManager, resourceManager);

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        hpLabel = new Label("HP: " + levelManager.getBaseHealth(), labelStyle);

        stage.getViewport().setCamera(uiCamera);
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
<<<<<<< Updated upstream
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        levelManager.update(delta);

        batch.begin();
        worldRenderer.render(batch, levelManager);
        batch.end();
=======
        renderMainCamera(delta);
        renderUICamera();
>>>>>>> Stashed changes

        hpLabel.setText("HP: " + levelManager.getBaseHealth());

        StageHelper.act(stage);
        stage.draw();
    }

    @Override
    public void show() {
        super.show(); 

        Gdx.input.setInputProcessor(stage);
        Texture texturaCastelo = MainUX.getTowerIcon().get(0);
        
        this.towerButton = new TowerButton(texturaCastelo, 0, 0, () -> {
            levelManager.positionTowerMode(texturaCastelo);
        });

        stage.addActor(towerButton);
        resize(1920, 1080);
    }

    private void renderMainCamera(float delta)
    {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        levelManager.update(delta);
        levelManager.draw(batch,shapeRenderer);
    }
    private void renderUICamera()
    {
        uiCamera.update();
        batch.setProjectionMatrix(uiCamera.combined);

    }

}
