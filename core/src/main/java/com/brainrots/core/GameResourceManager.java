package com.brainrots.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class GameResourceManager {

    private static AssetManager assetManager;

    public GameResourceManager(){
        assetManager = new AssetManager();
    }

    public void loadTextures(){
        assetManager.load("Maps/67Hills/67Hills.png", Texture.class);
        assetManager.load("temp.png", Texture.class);

        assetManager.finishLoading();
    }

    public static Texture getTexture(String name) {
        if (assetManager.isLoaded(name, Texture.class))
            return assetManager.get(name, Texture.class);
        return null;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
