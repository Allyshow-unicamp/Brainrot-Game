package com.brainrots.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.interfaces.GameSaveHandler;
import com.brainrots.interfaces.ScreenManager.ScreenType;
import com.brainrots.core.GlobalShop;
import com.brainrots.core.PlayerProfile;
import com.brainrots.core.ShopTowerData;
import com.brainrots.exception.InvalidGameActionException;
import com.brainrots.tower.BrainrotTower;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalShopScreen extends GameScreen {
    private GlobalShop globalShop;
    private PlayerProfile playerProfile;
    private GameSaveHandler saveHandler;
    private Label coinLabel;
    private Table itemsTable;
    private ScrollPane scrollPane;

    public GlobalShopScreen(SpriteBatch batch,
                            BrainRotGame screenManager, ShapeRenderer shapeRenderer,
                            GameSaveHandler saveHandler) {
        super(batch, screenManager, shapeRenderer);
        this.playerProfile = screenManager.getPlayerProfile();
        this.saveHandler = saveHandler;
        this.globalShop = new GlobalShop(saveHandler);
    }

    @Override
    public void show() {
        super.show();
        rebuildUI();
    }

    private void rebuildUI() {
        stage.clear();

        Table rootTable = createFullScreenTable();
        rootTable.pad(20);

        Label titleLabel = new Label("Game Store", createLabelStyle(4.0f, Color.YELLOW));
        rootTable.add(titleLabel).padBottom(10).center().row();

        coinLabel = new Label("Global Coins: " + playerProfile.getGlobalCoins(),
                createLabelStyle(2.5f, Color.GOLD));
        rootTable.add(coinLabel).padBottom(20).center().row();

        ButtonAssets buttons = createButtonAssets(1.2f);
        LabelStyle nameStyle = createLabelStyle(1.8f, Color.WHITE);
        LabelStyle behaviorStyle = createLabelStyle(1.3f, Color.CYAN);
        LabelStyle descStyle = createLabelStyle(1.0f, Color.LIGHT_GRAY);
        LabelStyle priceStyle = createLabelStyle(1.3f, Color.YELLOW);
        LabelStyle ownedStyle = createLabelStyle(1.3f, Color.GREEN);

        itemsTable = new Table();

        HashMap<String, ShopTowerData> shopData = globalShop.getShopData();
        List<String> unlockedIds = playerProfile.getUnlockedTowerIds();

        for (Map.Entry<String, ShopTowerData> entry : shopData.entrySet()) {
            ShopTowerData data = entry.getValue();
            boolean alreadyOwned = unlockedIds.contains(data.getId());

            Table rowTable = new Table();
            rowTable.defaults().pad(4);

            BrainrotTower towerType = findTowerById(data.getId());
            if (towerType != null) {
                Texture towerTexture = resourceManager.getTexture(towerType.getTexture());
                Image towerImage = new Image(new TextureRegionDrawable(new TextureRegion(towerTexture)));
                rowTable.add(towerImage).size(64, 64).padRight(8);
            }

            Table infoTable = new Table();
            infoTable.defaults().pad(2);
            Label nameLabel = new Label(data.getName(), nameStyle);
            infoTable.add(nameLabel).left().row();
            Label behaviorLabel = new Label("[" + data.getBehaviorDescription() + "]", behaviorStyle);
            infoTable.add(behaviorLabel).left().row();
            Label descLabel = new Label(data.getDescription(), descStyle);
            infoTable.add(descLabel).left();

            rowTable.add(infoTable).left().expandX();

            if (alreadyOwned) {
                Label ownedLabel = new Label("OWNED", ownedStyle);
                rowTable.add(ownedLabel).right().width(100);
            } else {
                Table actionTable = new Table();
                Label priceLabel = new Label(data.getPrice() + " Coins", priceStyle);
                actionTable.add(priceLabel).center().row();

                TextButton buyButton = new TextButton("Buy", buttons.style);
                String towerId = data.getId();
                buyButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        try {
                            globalShop.purchaseTower(playerProfile, towerId);
                            saveHandler.saveProfile(playerProfile);
                            rebuildUI();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                actionTable.add(buyButton).width(100).height(45);

                rowTable.add(actionTable).right();
            }

            itemsTable.add(rowTable).fillX().expandX().pad(6);
            itemsTable.row();
        }

        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        scrollPane = new ScrollPane(itemsTable, skin);
        scrollPane.setFadeScrollBars(false);
        rootTable.add(scrollPane).expand().fill().row();

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

    private BrainrotTower findTowerById(String towerId) {
        for (BrainrotTower tower : BrainrotTower.values()) {
            if (tower.getTowerId().equals(towerId)) {
                return tower;
            }
        }
        return null;
    }

    @Override
    protected void initializeHud() {
    }

    @Override
    protected void drawScreen(float delta) {
        drawWithStageHelper(delta);
    }
}
