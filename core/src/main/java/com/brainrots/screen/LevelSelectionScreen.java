package com.brainrots.screen;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.core.GameSaveHandler;
import com.brainrots.core.PlayerProfile;
import com.brainrots.exception.PersistanceException;
import com.brainrots.screen.ScreenManager.ScreenType;

public class LevelSelectionScreen extends GameScreen {
    private PlayerProfile playerProfile;
    private HashMap<String, String> levelHashMap;
    private Skin skin;

    public LevelSelectionScreen(SpriteBatch batch, GameResourceManager resourceManager, BrainRotGame screenManager,
            ShapeRenderer shapeRenderer, GameSaveHandler saveHandler) {
        super(batch, resourceManager, screenManager, shapeRenderer);

        this.playerProfile = screenManager.getPlayerProfile();
        this.skin = resourceManager.getSkin(Assets.UI_SKIN);
        try {
            this.levelHashMap = saveHandler.loadLevelData();
        } catch (PersistanceException e) {
            e.printStackTrace();
        }

        buildUI();
    }

     private void buildUI() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.pad(20);

        BitmapFont baseFont = resourceManager.getFont(Assets.UI_FONT);
        BitmapFont titleFont = new BitmapFont(baseFont.getData(), baseFont.getRegion(), true);
        titleFont.getData().setScale(4.0f);

        BitmapFont regularFont = new BitmapFont(baseFont.getData(), baseFont.getRegion(), true);
        regularFont.getData().setScale(2.0f);

        LabelStyle titleStyle = new LabelStyle(skin.get(LabelStyle.class));
        titleStyle.font = titleFont;
        titleStyle.fontColor = Color.YELLOW;

        LabelStyle itemStyle = new LabelStyle(skin.get(LabelStyle.class));
        itemStyle.font = regularFont;
        itemStyle.fontColor = Color.WHITE;

        TextButtonStyle buttonStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
        buttonStyle.font = regularFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.downFontColor = Color.YELLOW;

        Label titleLabel = new Label("Level Selection", titleStyle);
        rootTable.add(titleLabel).padBottom(40).colspan(2).center().row();

        Table itemsTable = new Table();
        itemsTable.defaults().pad(15);

        Map<String, String> sortedMap = new TreeMap<String, String>(levelHashMap);
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            Integer itemNumber = Integer.parseInt(String.valueOf(entry.getKey()));
            String itemName = entry.getValue();
            Integer stars = playerProfile.getLevelStars().get(String.valueOf(entry.getKey()));

            // se nível ainda não está desbloqueado
            if (itemNumber != 1 && stars < 1 && playerProfile.getLevelStars().get(String.valueOf(itemNumber - 1)) < 1)
                break;

            Label numberLabel = new Label("Level " + itemNumber, itemStyle);
            Label nameLabel = new Label(itemName, itemStyle);
            Label starsLabel = new Label(stars + "/3 Stars", itemStyle);
            TextButton playButton = new TextButton("Play", buttonStyle);

            playButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.selectLevel(itemNumber, itemName);
                }
            });

            itemsTable.add(numberLabel).left().width(250);
            itemsTable.add(nameLabel).right().width(250);
            itemsTable.add(starsLabel).width(250).height(60);
            itemsTable.add(playButton).width(250).height(60);
            itemsTable.row();
        }

        ScrollPane scrollPane = new ScrollPane(itemsTable, skin);
        scrollPane.setFadeScrollBars(false);
        rootTable.add(scrollPane).expand().fill().colspan(2).row();

        TextButton backButton = new TextButton("Back", buttonStyle);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.switchScreen(ScreenType.MAIN_MENU);
            }
        });

        rootTable.add(backButton).bottom().left().padTop(20).size(200, 60);

        stage.addActor(rootTable);
    }

    @Override
    protected void drawScreen(float delta) {
        StageHelper.act(stage);
        stage.draw();
    }
}
