package com.brainrots.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameResourceManager {

    private AssetManager assetManager;

    public GameResourceManager(){
        assetManager = new AssetManager();
    }

    public void loadAssets(){
        assetManager.load("Maps/67Hills/67Hills.png", Texture.class);
        assetManager.load("Enemies/temp.png", Texture.class);
        assetManager.load("Towers/tung-tung-tung-sahur.png", Texture.class);
        assetManager.load("Projectiles/black.png", Texture.class);

        assetManager.load("Soundtracks/game_song.mp3", Music.class);

        assetManager.load("UI/uiskin.json", Skin.class);

        assetManager.load("UI/default.fnt", BitmapFont.class);

        assetManager.finishLoading();
    }

    public Texture getTexture(String name) {
        if (assetManager.isLoaded(name, Texture.class))
            return assetManager.get(name, Texture.class);
        return null;
    }

    public Music getSound(String name) {
        if (assetManager.isLoaded(name, Music.class))
            return assetManager.get(name, Music.class);
        return null;
    }

    public Skin getSkin(String name) {
        if (assetManager.isLoaded(name, Skin.class))
            return assetManager.get(name, Skin.class);
        return null;
    }

    public BitmapFont getFont(String name) {
        if (assetManager.isLoaded(name, BitmapFont.class))
            return assetManager.get(name, BitmapFont.class);
        return null;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
