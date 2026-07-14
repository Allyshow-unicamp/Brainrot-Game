package com.brainrots.screen;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.interfaces.GameSaveHandler;
import com.brainrots.interfaces.ScreenManager.ScreenType;
import com.brainrots.core.PlayerProfile;
import com.brainrots.exception.PersistanceException;

public class LevelSelectionScreen extends GameScreen {
    private PlayerProfile playerProfile;
    private HashMap<String, String> levelHashMap;
    private Skin skin;

    public LevelSelectionScreen(SpriteBatch batch, BrainRotGame screenManager,
            ShapeRenderer shapeRenderer, GameSaveHandler saveHandler) {
        super(batch, screenManager, shapeRenderer);

        this.playerProfile = screenManager.getPlayerProfile();
        this.skin = resourceManager.getSkin(Assets.UI_SKIN);
        try {
            this.levelHashMap = saveHandler.loadLevelData();
        } catch (PersistanceException e) {
            e.printStackTrace();
        }

        initializeHud();
    }

     protected void initializeHud() {
        Table rootTable = createFullScreenTable();
        rootTable.pad(20);

        Label titleLabel = new Label("Level Selection", createLabelStyle(4.0f, Color.YELLOW));
        ButtonAssets buttons = createButtonAssets(2.0f);

        LabelStyle itemStyle = createLabelStyle(2.0f, Color.WHITE);
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
            TextButton playButton = new TextButton("Play", buttons.style);

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

        TextButton backButton = new TextButton("Back", buttons.style);
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
        batch.begin();

        Texture texture = resourceManager.getTexture(Assets.SELECTION_SCREEN);

        batch.draw(texture, 0, 0);
        batch.end();
        drawWithStageHelper(delta);
    }
}
