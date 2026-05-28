package com.brainrots.screen;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.LevelManager;

public class GameplayScreen extends GameScreen {

    private OrthographicCamera camera;
    private LevelManager levelManager;

    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager, ScreenManager screenManager){
        super(batch, resourceManager, screenManager);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        levelManager = new LevelManager(resourceManager, "67Hills");
    }

    @Override
    protected void drawScreen(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        levelManager.update(delta);
        levelManager.draw(batch);
    }
}
