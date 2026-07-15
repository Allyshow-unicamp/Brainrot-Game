package com.brainrots.MainUX;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.engine.CoinManager;
import com.brainrots.tower.Tower;

/** Painel de upgrade e venda de torres. 3 eixos de upgrade + botão de venda. */
public class TowerUpgradePanel extends Table {

    private Tower currentTower;
    private CoinManager coinManager;
    private Skin skin;

    private Image towerImage;
    private Label nameLabel;
    private Label descLabel;
    private TextButton damageBtn;
    private TextButton rangeBtn;
    private TextButton cooldownBtn;
    private ImageButton closeButton;
    private boolean sellRequest = false;
    private Label sellPrice;
    private Texture closeTexture;
    private Runnable onCloseCallback;

    public TowerUpgradePanel(Skin skin, CoinManager coinManager) {
        this.skin = skin;
        this.coinManager = coinManager;
        setVisible(false);
        createCloseTexture();
        buildLayout();
    }

    public boolean isSellRequested()
    {
        return sellRequest;
    }

    public void clearSellRequest()
    {
        sellRequest = false;
    }

    private Drawable createRedDrawable()
    {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.8f, 0.1f, 0.1f, 1f);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(tex));
    }

    private void createCloseTexture() {
        Pixmap pixmap = new Pixmap(24, 24, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        pixmap.setColor(1, 1, 1, 1);
        pixmap.drawLine(4, 4, 20, 20);
        pixmap.drawLine(20, 4, 4, 20);
        closeTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    private void buildLayout() {
        top().left();
        setBackground(createBgDrawable());

        GameResourceManager rm = GameResourceManager.getGameResourceManager();

        BitmapFont btnFont = rm.createFont(Assets.UI_FONT, 1.0f);
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        btnStyle.font = btnFont;

        BitmapFont descFont = rm.createFont(Assets.UI_FONT, 0.8f);
        Label.LabelStyle nameStyle = new Label.LabelStyle(btnFont, Color.WHITE);
        Label.LabelStyle descStyle = new Label.LabelStyle(descFont, Color.LIGHT_GRAY);

        ImageButton.ImageButtonStyle closeStyle = new ImageButton.ImageButtonStyle();
        closeStyle.imageUp = new TextureRegionDrawable(new TextureRegion(closeTexture));
        closeButton = new ImageButton(closeStyle);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
                if (onCloseCallback != null) {
                    onCloseCallback.run();
                }
            }
        });

        towerImage = new Image();
        nameLabel = new Label("", nameStyle);
        descLabel = new Label("", descStyle);
        descLabel.setWrap(true);

        damageBtn = new TextButton("", btnStyle);
        rangeBtn = new TextButton("", btnStyle);
        cooldownBtn = new TextButton("", btnStyle);

        setupButton(damageBtn, () -> {
            if (currentTower != null) {
                int cost = currentTower.getDamageUpgradeCost();
                if (coinManager.canAfford(cost)) {
                    currentTower.upgradeDamage();
                    coinManager.subtractCoins(cost);
                    currentTower.addValueSpent(cost);
                    refresh();
                }
            }
        });

        setupButton(rangeBtn, () -> {
            if (currentTower != null) {
                int cost = currentTower.getRangeUpgradeCost();
                if (coinManager.canAfford(cost)) {
                    currentTower.upgradeRange();
                    coinManager.subtractCoins(cost);
                    currentTower.addValueSpent(cost);
                    refresh();
                }
            }
        });

        setupButton(cooldownBtn, () -> {
            if (currentTower != null) {
                int cost = currentTower.getCooldownUpgradeCost();
                if (coinManager.canAfford(cost))
                {
                    currentTower.upgradeCooldown();
                    coinManager.subtractCoins(cost);
                    currentTower.addValueSpent(cost);
                    refresh();
                }
            }
        });

        add(closeButton).size(24, 24).top().right().expandX();
        row();
        add(towerImage).size(80, 80).padTop(10).colspan(1).center();
        row();
        add(nameLabel).padTop(5).center();
        row();
        add(descLabel).padTop(3).padLeft(5).padRight(5).fillX().center();
        row();
        add(damageBtn).fillX().padTop(15).padLeft(5).padRight(5);
        row();
        add(rangeBtn).fillX().padTop(5).padLeft(5).padRight(5);
        row();
        add(cooldownBtn).fillX().padTop(5).padLeft(5).padRight(5);
        row();
        createSellButtonSection(btnStyle, new Label.LabelStyle(descFont, Color.GOLD));
    }

    private void createSellButtonSection(TextButton.TextButtonStyle baseBtnStyle, Label.LabelStyle priceStyle) {

    TextButton.TextButtonStyle sellBtnStyle = new TextButton.TextButtonStyle(baseBtnStyle);
    sellBtnStyle.up = createRedDrawable();
    sellBtnStyle.fontColor = Color.WHITE;

    TextButton sellBtn = new TextButton("Sell", sellBtnStyle);
    sellPrice = new Label("", priceStyle);
    sellPrice.setColor(Color.GOLD);
    sellBtn.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (currentTower != null) {
                coinManager.addCoins(currentTower.getValueSpent());
                sellRequest = true;
            }
        }
    });

    Table sellContainer = new Table();
    sellContainer.defaults().pad(4);
    sellContainer.add(sellBtn).width(90).height(35);
    sellContainer.add(sellPrice).left();

    row();
    add(sellContainer).fillX().padTop(25).bottom().center();
}

    private void setupButton(TextButton button, Runnable action) {
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
    }

    private Drawable createBgDrawable() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.08f, 0.08f, 0.15f, 0.9f);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(tex));
    }

    public void open(Tower tower) {
        currentTower = tower;
        TextureRegion region = tower.getTextureRegion();
        towerImage.setDrawable(new TextureRegionDrawable(region));
        refresh();
        setVisible(true);
    }

    public void close() {
        currentTower = null;
        setVisible(false);
    }

    public boolean isOpen() {
        return isVisible() && currentTower != null;
    }

    public Tower getSelectedTower() {
        return currentTower;
    }

    public void setOnCloseCallback(Runnable callback) {
        onCloseCallback = callback;
    }

    public void refresh() {
        if (currentTower == null) return;
        nameLabel.setText(currentTower.getName());
        descLabel.setText(currentTower.getDescription());

        damageBtn.setText(currentTower.getDamageUpgradeLabel());
        rangeBtn.setText(currentTower.getRangeUpgradeLabel());
        cooldownBtn.setText(currentTower.getCooldownUpgradeLabel());

        damageBtn.setDisabled(!coinManager.canAfford(currentTower.getDamageUpgradeCost()));
        rangeBtn.setDisabled(!coinManager.canAfford(currentTower.getRangeUpgradeCost()));
        cooldownBtn.setDisabled(!coinManager.canAfford(currentTower.getCooldownUpgradeCost()));
        sellPrice.setText("$" + currentTower.getValueSpent());
    }

    public void resizePanel(float width, float height) {
        float panelWidth = width * 0.17f;
        float panelHeight = height * 0.85f;
        float panelX = width * 0.87f - panelWidth;
        float panelY = height * 0.15f;

        setSize(panelWidth, panelHeight);
        setPosition(panelX, panelY);
        invalidate();
    }
}
