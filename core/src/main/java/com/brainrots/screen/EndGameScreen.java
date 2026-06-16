package com.brainrots.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.interfaces.GameSaveHandler;
import com.brainrots.interfaces.StageHelper;
import com.brainrots.interfaces.ScreenManager.ScreenType;
import com.brainrots.core.PlayerProfile;
import com.brainrots.exception.PersistanceException;

public class EndGameScreen extends GameScreen {

    public EndGameScreen(SpriteBatch batch, GameResourceManager resourceManager, BrainRotGame screenManager,
            ShapeRenderer shapeRenderer, int level, boolean won, int stars, GameSaveHandler gameSaveHandler) {
        super(batch, screenManager, shapeRenderer);

        if (won)
            try {
                PlayerProfile playerProfile = screenManager.getPlayerProfile();
                playerProfile.setStars(String.valueOf(level), stars);
                gameSaveHandler.saveProfile(playerProfile);
            } catch (PersistanceException e) {
                e.printStackTrace();
            }

        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10);

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);

        BitmapFont baseFont = resourceManager.getFont(Assets.UI_FONT);
        BitmapFont titleFont = new BitmapFont(baseFont.getData(), baseFont.getRegion(), true);
        titleFont.getData().setScale(4.0f);
        BitmapFont regularFont = new BitmapFont(baseFont.getData(), baseFont.getRegion(), true);
        regularFont.getData().setScale(2.0f);

        LabelStyle titleStyle = new LabelStyle(skin.get(LabelStyle.class));
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.YELLOW;

        LabelStyle starsStyle = new LabelStyle(skin.get(LabelStyle.class));
        starsStyle.font = regularFont;
        starsStyle.fontColor = Color.WHITE;

        Label titleLabel = new Label(
                won ? "Victory" : "Defeat", titleStyle);
        table.add(titleLabel).pad(60).row();

        if (won) {
            Label starsLabel = new Label(stars + "/3 Stars", starsStyle);
            table.add(starsLabel).pad(60).row();
        }

        TextButtonStyle buttonStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
        buttonStyle.font = regularFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.YELLOW;

        TextButton backButton = new TextButton("Back", buttonStyle);
        table.add(backButton).size(200, 60);
        table.row();

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.switchScreen(ScreenType.LEVEL_SELECTION);
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
