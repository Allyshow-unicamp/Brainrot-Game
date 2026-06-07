package com.brainrots.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brainrots.core.GameResourceManager;

public class LevelSelectionScreen extends GameScreen {

    public LevelSelectionScreen(SpriteBatch batch, GameResourceManager resourceManager, ScreenManager screenManager,
            ShapeRenderer shapeRenderer) {
        super(batch, resourceManager, screenManager, shapeRenderer);
    }

    @Override
    protected void drawScreen(float delta) {}
}
