package com.brainrots.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.LevelManager;


public class GameplayScreen extends GameScreen {

    private OrthographicCamera camera;      // Camera principal do jogo
    private LevelManager levelManager;      // Gerenciador da fase
    private Label hpLabel;
    private TowerButton towerButton;

    public GameplayScreen(SpriteBatch batch, GameResourceManager resourceManager,
                BrainRotGame screenManager, ShapeRenderer shapeRenderer, int level, String levelName) {
        super(batch, resourceManager, screenManager, shapeRenderer);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        levelManager = new LevelManager(resourceManager, screenManager, level, levelName, camera);
        levelManager.startNextWave();

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        BitmapFont font = resourceManager.getFont(Assets.UI_FONT);
        font.getData().setScale(2.0f);
        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        labelStyle.font = font;
        hpLabel = new Label("HP: " + levelManager.getBaseHealth(), labelStyle);

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

}
