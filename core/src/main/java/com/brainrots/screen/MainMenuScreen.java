package com.brainrots.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.core.BrainRotGame;
import com.brainrots.interfaces.ScreenManager.ScreenType;

public class MainMenuScreen extends GameScreen {

    public MainMenuScreen(SpriteBatch batch, BrainRotGame screenManager,
            ShapeRenderer shapeRenderer) {
        super(batch, screenManager, shapeRenderer);

        Table table = createFullScreenTable();

        Label titleLabel = new Label(
                "The Lord of Brainrots: The Battle for Brainrot Village II: The Rise of the Brainrot King",
                createLabelStyle(2.0f, Color.YELLOW));
        table.add(titleLabel).pad(60).row();

        ButtonAssets buttons = createButtonAssets(2.0f);

        TextButton playButton = new TextButton("Play", buttons.style);
        table.add(playButton).size(200, 60);
        table.row();

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.switchScreen(ScreenType.LEVEL_SELECTION);
            }
        });

        TextButton storeButton = new TextButton("Store", buttons.style);
        table.add(storeButton).size(200, 60);

        storeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.switchScreen(ScreenType.GLOBAL_SHOP);
            }
        });

        stage.addActor(table);
    }

    @Override
    protected void initializeHud() {

    }

    @Override
    protected void drawScreen(float delta) {
        drawWithStageHelper(delta);
    }
}
