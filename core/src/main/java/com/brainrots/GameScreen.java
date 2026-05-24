package com.brainrots;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameScreen extends ScreenAdapter {

    protected final SpriteBatch batch;
    protected final GameResourceManager resourceManager;
    protected final ScreenManager screenManager;

    public GameScreen(SpriteBatch batch, GameResourceManager resourceManager, ScreenManager screenManager){
        this.batch = batch;
        this.resourceManager = resourceManager;
        this.screenManager = screenManager;
    }

    @Override
    public void show() {

    }

    @Override
    public final void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawScreen(delta);
    }

    protected abstract void drawScreen(float delta);

    @Override
    public void dispose() {
        super.dispose();
    }
}
