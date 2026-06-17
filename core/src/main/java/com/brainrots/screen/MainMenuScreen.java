package com.brainrots.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.interfaces.StageHelper;
import com.brainrots.interfaces.ScreenManager.ScreenType;

public class MainMenuScreen extends GameScreen {

    public MainMenuScreen(SpriteBatch batch, BrainRotGame screenManager,
            ShapeRenderer shapeRenderer) {
        super(batch, screenManager, shapeRenderer);

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10);

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        BitmapFont font = resourceManager.getFont(Assets.UI_FONT);

        LabelStyle labelStyle = new LabelStyle(skin.get(LabelStyle.class));
        font.getData().setScale(4.0f);
        labelStyle.font = font;
        labelStyle.fontColor = Color.YELLOW;

        Label titleLabel = new Label(
                "The Lord of Brainrots: The Battle for Brainrot Village II: The Rise of the Brainrot King", labelStyle);
        table.add(titleLabel).pad(60).row();

        TextButtonStyle buttonStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
        font.getData().setScale(2.0f);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.YELLOW;

        TextButton playButton = new TextButton("Play", buttonStyle);
        table.add(playButton).size(200, 60);
        table.row();

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.switchScreen(ScreenType.LEVEL_SELECTION);
            }
        });

        TextButton storeButton = new TextButton("Store", buttonStyle);
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
        StageHelper.act(stage);
        stage.draw();
    }
}
