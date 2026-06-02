package com.brainrots.screen;

import java.util.ArrayDeque;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.LevelManager;
import com.brainrots.tower.ConcreteTower;
import com.brainrots.tower.TargetType;

import java.util.Vector;

public class GameplayScreen extends GameScreen {

    private OrthographicCamera camera;
    private OrthographicCamera uiCamera; //importtante pra n zikar o jogo, confia
    private LevelManager levelManager;
    private Label hpLabel;
    private TowerButton towerButton;

    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager, 
                ScreenManager screenManager, ShapeRenderer shapeRenderer) {
        super(batch, resourceManager, screenManager,shapeRenderer);

        camera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
        uiCamera.setToOrtho(false,1920,1080);
        levelManager = new LevelManager(resourceManager, screenManager, "67Hills",camera);
        String enemy = "Enemies/temp.png";
        levelManager.startNextWave(new ArrayDeque<>(List.of(enemy, enemy, enemy, enemy)));

        Texture towTexture = resourceManager.getTexture("Towers/tung-tung-tung-sahur.png");
        ConcreteTower concreteTower = new ConcreteTower(towTexture, new Vector2(500, 500), 25, 2000, 250, 0.5f,
                150, 150, TargetType.SINGLE, levelManager, resourceManager);
        levelManager.buyAndPlaceTower(concreteTower);

        Skin skin = resourceManager.getSkin("UI/uiskin.json");
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
        renderMainCamera(delta);
        renderUICamera();

        hpLabel.setText("HP: " + levelManager.getBaseHealth());

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        resize(1920,1080);
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
