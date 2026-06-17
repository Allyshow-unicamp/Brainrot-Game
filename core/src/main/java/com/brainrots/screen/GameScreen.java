package com.brainrots.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;

public abstract class GameScreen extends ScreenAdapter {

    protected final SpriteBatch batch;
    protected final Stage stage;
    protected final GameResourceManager resourceManager;
    protected final BrainRotGame screenManager;
    protected final ShapeRenderer shapeRenderer;

    public GameScreen(SpriteBatch batch, BrainRotGame screenManager, ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.resourceManager = GameResourceManager.getGameResourceManager();
        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    protected abstract void initializeHud();

    @Override
    public final void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawScreen(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    protected abstract void drawScreen(float delta);

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
