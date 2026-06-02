package com.brainrots.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameResourceManager {

    private AssetManager assetManager;
    private static GameResourceManager resourceManager;

    private GameResourceManager()
    {
        this.assetManager = new AssetManager();
    }

    public static GameResourceManager getGameResourceManager()
    {
        if(resourceManager == null)
            resourceManager = new GameResourceManager();
        return resourceManager;
    }

    public void loadAssets() {
        assetManager.load(Assets.MAP_67_HILLS_BG, Texture.class);
        assetManager.load(Assets.ENEMY_TEMP, Texture.class);
        assetManager.load(Assets.TOWER_TUNG, Texture.class);
        assetManager.load(Assets.PROJECTILE_BLACK, Texture.class);

        assetManager.load(Assets.MUSIC_GAME, Music.class);

        assetManager.load(Assets.UI_SKIN, Skin.class);
        assetManager.load(Assets.UI_FONT, BitmapFont.class);

        assetManager.finishLoading();
    }

    public Texture getTexture(String name) {
        if (assetManager.isLoaded(name, Texture.class)) {
            return assetManager.get(name, Texture.class);
        }
        return null;
    }

    public Music getSound(String name) {
        if (assetManager.isLoaded(name, Music.class)) {
            return assetManager.get(name, Music.class);
        }
        return null;
    }

    public Skin getSkin(String name) {
        if (assetManager.isLoaded(name, Skin.class)) {
            return assetManager.get(name, Skin.class);
        }
        return null;
    }

    public BitmapFont getFont(String name) {
        if (assetManager.isLoaded(name, BitmapFont.class)) {
            return assetManager.get(name, BitmapFont.class);
        }
        return null;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
