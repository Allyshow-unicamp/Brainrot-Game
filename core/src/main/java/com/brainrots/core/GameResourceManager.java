package com.brainrots.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameResourceManager {

    private AssetManager assetManager;

    public GameResourceManager(){
        assetManager = new AssetManager();
    }

    public void loadAssets(){
        assetManager.load("Maps/67Hills/67Hills.png", Texture.class);
        assetManager.load("temp.png", Texture.class);

        assetManager.load("Soundtracks/game_song.mp3", Music.class);

        assetManager.load("UI/uiskin.json", Skin.class);

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

    public void dispose() {
        assetManager.dispose();
    }
}
