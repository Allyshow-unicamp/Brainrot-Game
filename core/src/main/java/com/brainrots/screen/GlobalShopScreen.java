package com.brainrots.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.core.GlobalShop;
import com.brainrots.core.PlayerProfile;
import com.brainrots.exception.InvalidGameActionException;
import com.brainrots.screen.ScreenManager.ScreenType;

import java.nio.file.Paths;
import java.util.Map;

public class GlobalShopScreen extends GameScreen {

    private GlobalShop globalShop;
    private Skin skin;
    private PlayerProfile profile;

    public GlobalShopScreen(SpriteBatch batch, GameResourceManager resourceManager,
                            ScreenManager screenManager, ShapeRenderer shapeRenderer, PlayerProfile profile) {
        super(batch, resourceManager, screenManager, shapeRenderer);
        this.profile = profile;

        String shopPath = Paths.get("data", "shop-data.json").toString();
        this.globalShop = new GlobalShop(shopPath);

        this.skin = resourceManager.getSkin(Assets.UI_SKIN);

        buildUI();
    }

    private void buildUI() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.pad(20);

        Label titleLabel = new Label("Game Store", skin);
        titleLabel.setFontScale(2.0f);
        rootTable.add(titleLabel).padBottom(20).colspan(2).center().row();

        Table itemsTable = new Table();
        itemsTable.defaults().pad(10);

        Map<String, Integer> itemPrices = globalShop.getItemPrices();
        for (Map.Entry<String, Integer> entry : itemPrices.entrySet()) {
            String itemName = entry.getKey();
            Integer itemPrice = entry.getValue();

            Label nameLabel = new Label(itemName, skin);
            Label priceLabel = new Label(itemPrice + " Coins", skin);
            TextButton buyButton = new TextButton("Buy", skin);

            buyButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    try {
                        globalShop.purchaseTower(profile, itemName);
                    } catch (InvalidGameActionException e) {
                        e.printStackTrace();
                    }
                }
            });

            itemsTable.add(nameLabel).left().width(300);
            itemsTable.add(priceLabel).right().width(150);
            itemsTable.add(buyButton).width(100);
            itemsTable.row();
        }

        ScrollPane scrollPane = new ScrollPane(itemsTable, skin);
        scrollPane.setFadeScrollBars(false);
        rootTable.add(scrollPane).expand().fill().colspan(2).row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.switchScreen(ScreenType.MAIN_MENU);
            }
        });

        rootTable.add(backButton).bottom().left().padTop(20).size(150, 50);

        stage.addActor(rootTable);
    }

    @Override
    protected void drawScreen(float delta) {
        stage.act(delta);
        stage.draw();
    }
}
