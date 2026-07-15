package com.brainrots.MainUX;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;

/** Modal de confirmação reutilizável (Yes/No). */
public class ConfirmModal extends Table {

    private Runnable onConfirm;
    private Runnable onCancel;

    public ConfirmModal(Skin skin, String title, String message) {
        setFillParent(true);
        setBackground(createOverlayDrawable());

        Table contentTable = new Table();
        contentTable.setBackground(createContentBgDrawable());

        GameResourceManager rm = GameResourceManager.getGameResourceManager();
        BitmapFont titleFont = rm.createFont(Assets.UI_FONT, 1.5f);
        BitmapFont msgFont = rm.createFont(Assets.UI_FONT, 1.0f);
        BitmapFont btnFont = rm.createFont(Assets.UI_FONT, 1.2f);

        Label.LabelStyle titleStyle = new Label.LabelStyle(titleFont, Color.WHITE);
        Label.LabelStyle msgStyle = new Label.LabelStyle(msgFont, Color.LIGHT_GRAY);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        btnStyle.font = btnFont;

        Label titleLabel = new Label(title, titleStyle);
        Label msgLabel = new Label(message, msgStyle);
        msgLabel.setWrap(true);

        TextButton yesBtn = new TextButton("Yes", btnStyle);
        yesBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onConfirm != null) onConfirm.run();
            }
        });

        TextButton noBtn = new TextButton("No", btnStyle);
        noBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (onCancel != null) onCancel.run();
            }
        });

        contentTable.defaults().pad(10);
        contentTable.add(titleLabel).center();
        contentTable.row();
        contentTable.add(msgLabel).center().fillX().padLeft(20).padRight(20);
        contentTable.row();
        Table btnRow = new Table();
        btnRow.add(yesBtn).size(100, 50).padRight(15);
        btnRow.add(noBtn).size(100, 50);
        contentTable.add(btnRow).center().padTop(10);

        add(contentTable).pad(30).center();

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (event.getTarget() == ConfirmModal.this) {
                    if (onCancel != null) onCancel.run();
                }
            }
        });

        setVisible(false);
    }

    public void setOnConfirm(Runnable callback) {
        this.onConfirm = callback;
    }

    public void setOnCancel(Runnable callback) {
        this.onCancel = callback;
    }

    public void show() {
        setVisible(true);
    }

    public void hide() {
        setVisible(false);
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
}
