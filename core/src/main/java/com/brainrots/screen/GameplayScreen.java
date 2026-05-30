package com.brainrots.screen;

import java.util.ArrayDeque;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.LevelManager;
import com.brainrots.tower.ConcreteTower;
import com.brainrots.tower.TargetType;

public class GameplayScreen extends GameScreen {

    private OrthographicCamera camera;
    private LevelManager levelManager;
    private Label hpLabel;

    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager, ScreenManager screenManager) {
        super(batch, resourceManager, screenManager);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        levelManager = new LevelManager(resourceManager, screenManager, "67Hills");
        String enemy = "Enemies/temp.png";
        levelManager.startNextWave(new ArrayDeque<>(List.of(enemy, enemy, enemy, enemy)));

        Texture towTexture = resourceManager.getTexture("Towers/tung-tung-tung-sahur.png");
        ConcreteTower concreteTower = new ConcreteTower(towTexture, new Vector2(500, 500), 25, 2000, 250, 0.5f,
                150, 150, TargetType.SINGLE, levelManager, resourceManager);
        levelManager.buyAndPlaceTower(concreteTower);

        Skin skin = resourceManager.getSkin("UI/uiskin.json");
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
        levelManager.draw(batch);

        hpLabel.setText("HP: " + levelManager.getBaseHealth());

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
}
