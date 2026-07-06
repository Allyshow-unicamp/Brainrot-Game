package com.brainrots.MainUX;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.tower.BrainrotTower;

public class TowerInfoModal extends Table {

    private Runnable onCloseCallback;
    private Skin skin;
    private Table contentTable;
    private Texture closeTexture;
    private Image towerImage;
    private Label nameLabel;
    private Label descLabel;
    private Label damageValLabel;
    private Label rangeValLabel;
    private Label cooldownValLabel;
    private Label costValLabel;
    private Label damageUpgradeLabel;
    private Label rangeUpgradeLabel;
    private Label cooldownUpgradeLabel;

    public TowerInfoModal(Skin skin) {
        this.skin = skin;
        setFillParent(true);
        setBackground(createOverlayDrawable());

        contentTable = new Table();
        contentTable.setBackground(createContentBgDrawable());

        GameResourceManager rm = GameResourceManager.getGameResourceManager();
        BitmapFont titleFont = rm.createFont(Assets.UI_FONT, 1.5f);
        BitmapFont descFont = rm.createFont(Assets.UI_FONT, 1.0f);
        BitmapFont statLabelFont = rm.createFont(Assets.UI_FONT, 0.9f);
        BitmapFont upgradeFont = rm.createFont(Assets.UI_FONT, 0.85f);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle descStyle = new Label.LabelStyle(descFont, Color.LIGHT_GRAY);
        Label.LabelStyle statLabelStyle = new Label.LabelStyle(statLabelFont, Color.CYAN);
        Label.LabelStyle statValStyle = new Label.LabelStyle(statLabelFont, Color.YELLOW);
        Label.LabelStyle upgradeTitleStyle = new Label.LabelStyle(upgradeFont, Color.GREEN);
        Label.LabelStyle upgradeDescStyle = new Label.LabelStyle(upgradeFont, Color.WHITE);

        createCloseTexture();
        ImageButton.ImageButtonStyle closeStyle = new ImageButton.ImageButtonStyle();
        closeStyle.imageUp = new TextureRegionDrawable(new TextureRegion(closeTexture));
        ImageButton closeButton = new ImageButton(closeStyle);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        towerImage = new Image();
        nameLabel = new Label("", titleStyle);
        descLabel = new Label("", descStyle);
        descLabel.setWrap(true);

        damageValLabel = new Label("", statValStyle);
        rangeValLabel = new Label("", statValStyle);
        cooldownValLabel = new Label("", statValStyle);
        costValLabel = new Label("", statValStyle);

        damageUpgradeLabel = new Label("", upgradeDescStyle);
        rangeUpgradeLabel = new Label("", upgradeDescStyle);
        cooldownUpgradeLabel = new Label("", upgradeDescStyle);

        contentTable.top().left();
        contentTable.defaults().pad(4);
        contentTable.add(closeButton).size(24, 24).top().right().expandX();
        contentTable.row();
        contentTable.add(towerImage).size(100, 100).padTop(10).center();
        contentTable.row();
        contentTable.add(nameLabel).padTop(8).center();
        contentTable.row();
        contentTable.add(descLabel).padTop(4).padLeft(10).padRight(10).center().fillX();
        contentTable.row();

        Table statTable = new Table();
        statTable.defaults().pad(2);
        statTable.add(new Label("Damage:", statLabelStyle)).left();
        statTable.add(damageValLabel).left().width(60);
        statTable.add(new Label("Range:", statLabelStyle)).left();
        statTable.add(rangeValLabel).left();
        statTable.row();
        statTable.add(new Label("Cooldown:", statLabelStyle)).left();
        statTable.add(cooldownValLabel).left();
        statTable.add(new Label("Cost:", statLabelStyle)).left();
        statTable.add(costValLabel).left();

        contentTable.add(statTable).padTop(10).center();
        contentTable.row();
        contentTable.add(new Label("--- Upgrades ---", upgradeTitleStyle)).padTop(12).center();
        contentTable.row();
        contentTable.add(damageUpgradeLabel).padLeft(10).left();
        contentTable.row();
        contentTable.add(rangeUpgradeLabel).padLeft(10).left();
        contentTable.row();
        contentTable.add(cooldownUpgradeLabel).padLeft(10).left();
        contentTable.row();
        contentTable.add(new Label("Base cost: $50 (Damage), $40 (Range), $60 (Speed)", new Label.LabelStyle(upgradeFont, Color.GOLD))).padTop(8).center();

        contentTable.pack();

        add(contentTable).pad(30).center();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getTarget() == TowerInfoModal.this) {
                    close();
                }
            }
        });

        setVisible(false);
    }

    public void showTower(BrainrotTower towerType) {
        GameResourceManager rm = GameResourceManager.getGameResourceManager();
        Texture towerTexture = rm.getTexture(towerType.getTexture());
        towerImage.setDrawable(new TextureRegionDrawable(new TextureRegion(towerTexture)));

        nameLabel.setText(towerType.getDisplayName());
        descLabel.setText(towerType.getDescription());
        damageValLabel.setText(towerType.getDamage() > 0 ? String.valueOf(towerType.getDamage()) : "N/A");
        rangeValLabel.setText(String.format("%.0f", towerType.getRange()));
        cooldownValLabel.setText(String.format("%.1fs", towerType.getAttackCooldown()));
        costValLabel.setText("$" + towerType.getCost());

        switch (towerType) {
            case CAPPUCCINO_ASSASSINO:
                damageUpgradeLabel.setText("Coins/Tower: +3 per level");
                break;
            case BALLERINA_CAPPUCCINA:
                damageUpgradeLabel.setText("Boost: +5 per level");
                break;
            default:
                damageUpgradeLabel.setText("Damage: +10 per level");
                break;
        }
        rangeUpgradeLabel.setText("Range: +25 per level");
        cooldownUpgradeLabel.setText("Speed: -15% per level");

        setVisible(true);
    }

    private void createCloseTexture() {
        Pixmap pixmap = new Pixmap(24, 24, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0);
        pixmap.fill();
        pixmap.setColor(1, 0, 0, 1);
        pixmap.drawLine(4, 4, 20, 20);
        pixmap.drawLine(20, 4, 4, 20);
        closeTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    private Drawable createOverlayDrawable() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.6f);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(tex));
    }

    private Drawable createContentBgDrawable() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.12f, 0.12f, 0.25f, 0.95f);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return new TextureRegionDrawable(new TextureRegion(tex));
    }

    public void close() {
        setVisible(false);
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }
}
