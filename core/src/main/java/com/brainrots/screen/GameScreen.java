package com.brainrots.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.interfaces.StageHelper;

public abstract class GameScreen extends ScreenAdapter {

    protected final SpriteBatch batch;
    protected final Stage stage;
    protected final GameResourceManager resourceManager;
    protected final BrainRotGame screenManager;
    protected final ShapeRenderer shapeRenderer;

    public GameScreen(SpriteBatch batch, BrainRotGame screenManager, ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.resourceManager = GameResourceManager.getGameResourceManager();
        this.screenManager = screenManager;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    protected Table createFullScreenTable() {
        Table table = new Table();
        table.setFillParent(true);
        table.defaults().pad(10);
        return table;
    }

    protected ButtonAssets createButtonAssets(float scale) {
        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        BitmapFont font = resourceManager.createFont(Assets.UI_FONT, scale);
        TextButtonStyle style = new TextButtonStyle(skin.get(TextButtonStyle.class));
        style.font = font;
        style.fontColor = Color.WHITE;
        style.downFontColor = Color.YELLOW;
        return new ButtonAssets(font, style);
    }

    protected LabelStyle createLabelStyle(float scale, Color color) {
        Skin skin = resourceManager.getSkin(Assets.UI_SKIN);
        BitmapFont font = resourceManager.createFont(Assets.UI_FONT, scale);
        LabelStyle style = new LabelStyle(skin.get(LabelStyle.class));
        style.font = font;
        style.fontColor = color;
        return style;
    }

    protected void drawWithStageHelper(float delta) {
        StageHelper.act(stage);
        stage.draw();
    }

    protected static class ButtonAssets {
        public final BitmapFont font;
        public final TextButtonStyle style;
        public ButtonAssets(BitmapFont font, TextButtonStyle style) {
            this.font = font;
            this.style = style;
        }
    }

    protected abstract void initializeHud();

    @Override
    public final void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawScreen(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    protected abstract void drawScreen(float delta);

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
