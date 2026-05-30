package com.brainrots.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.LevelLoader;
import com.brainrots.engine.LevelManager;
import com.brainrots.engine.WorldRenderer;
import com.brainrots.screen.ScreenManager.ScreenType;

public class GameplayScreen extends GameScreen {

    private static final int VIEWPORT_WIDTH = 1920;
    private static final int VIEWPORT_HEIGHT = 1080;

    private final OrthographicCamera camera = new OrthographicCamera();
    private final WorldRenderer worldRenderer = new WorldRenderer();
    private final LevelManager levelManager;
    private final Label hpLabel;

    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager, ScreenManager screenManager) {
        super(batch, resourceManager, screenManager);

        Runnable runOnEnd = () -> screenManager.switchScreen(ScreenType.MAIN_MENU);

        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        levelManager = new LevelManager(resourceManager, Assets.MAP_67_HILLS_ID, runOnEnd);
        LevelLoader.loadDefaultLevel(levelManager, resourceManager);

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        hpLabel = new Label("HP: " + levelManager.getBaseHealth(), labelStyle);

        stage.addActor(hpLabel);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        hpLabel.setPosition(10, stage.getHeight() - hpLabel.getHeight() - 10);
    }

    @Override
    protected void drawScreen(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        levelManager.update(delta);

        batch.begin();
        worldRenderer.render(batch, levelManager);
        batch.end();

        hpLabel.setText("HP: " + levelManager.getBaseHealth());

        StageHelper.act(stage);
        stage.draw();
    }
}
