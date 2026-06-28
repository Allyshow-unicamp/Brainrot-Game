package com.brainrots.screen;

import com.badlogic.gdx.graphics.Color;
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
import com.brainrots.core.GlobalShop;
import com.brainrots.core.PlayerProfile;
import com.brainrots.exception.InvalidGameActionException;

import java.util.HashMap;
import java.util.Map;

public class GlobalShopScreen extends GameScreen {
    private GlobalShop globalShop;
    private PlayerProfile playerProfile;

    public GlobalShopScreen(SpriteBatch batch,
                            BrainRotGame screenManager, ShapeRenderer shapeRenderer,
                            GameSaveHandler saveHandler) {
        super(batch, screenManager, shapeRenderer);
        this.playerProfile = screenManager.getPlayerProfile();
        this.globalShop = new GlobalShop(saveHandler);

        initializeHud();
    }

    @Override
    protected void initializeHud() {
        Table rootTable = createFullScreenTable();
        rootTable.pad(20);

        Label titleLabel = new Label("Game Store", createLabelStyle(4.0f, Color.YELLOW));
        ButtonAssets buttons = createButtonAssets(2.0f);
        LabelStyle itemStyle = createLabelStyle(2.0f, Color.WHITE);
        rootTable.add(titleLabel).padBottom(40).colspan(2).center().row();

        Table itemsTable = new Table();
        itemsTable.defaults().pad(15);

        HashMap<String, Integer> itemPrices = globalShop.getItemPrices();
        for (Map.Entry<String, Integer> entry : itemPrices.entrySet()) {
            String itemName = entry.getKey();
            Integer itemPrice = entry.getValue();

            Label nameLabel = new Label(itemName, itemStyle);
            Label priceLabel = new Label(itemPrice + " Coins", itemStyle);
            TextButton buyButton = new TextButton("Buy", buttons.style);

            buyButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    try {
                        globalShop.purchaseTower(playerProfile, itemName);
                    } catch (InvalidGameActionException e) {
                        e.printStackTrace();
                    }
                }
            });

            itemsTable.add(nameLabel).left().width(400);
            itemsTable.add(priceLabel).right().width(200);
            itemsTable.add(buyButton).width(150).height(60);
            itemsTable.row();
        }

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
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
        drawWithStageHelper(delta);
    }
}
