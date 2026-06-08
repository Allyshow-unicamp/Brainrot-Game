package com.brainrots.core;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/*
* Classe que gerencia os recursos do jogo.
*/
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

    /**
     * Carrega todos os assets do jogo.
     */
    public void loadAssets() {
        assetManager.load(Assets.MAP_67_HILLS_BG, Texture.class);
        assetManager.load(Assets.ENEMY_67, Texture.class);
        assetManager.load(Assets.ENEMY_6, Texture.class);
        assetManager.load(Assets.ENEMY_7, Texture.class);
        assetManager.load(Assets.ENEMY_6_7, Texture.class);
        assetManager.load(Assets.TOWER_TUNG, Texture.class);
        assetManager.load(Assets.PROJECTILE_BLACK, Texture.class);

        assetManager.load(Assets.MUSIC_GAME, Music.class);

        assetManager.load(Assets.UI_SKIN, Skin.class);
        assetManager.load(Assets.UI_FONT, BitmapFont.class);

        assetManager.finishLoading();
    }

    /**
     * Obtem uma textura já carregada.
     * @param name Caminho da textura.
     * @return Retorna a textura ou null.
     */
    public Texture getTexture(String name) {
        if (assetManager.isLoaded(name, Texture.class)) {
            return assetManager.get(name, Texture.class);
        }
        return null;
    }

    /**
     * Obtem o audio já carregado.
     * @param name Caminho do audio.
     * @return Retorna o audio ou null.
     */
    public Music getSound(String name) {
        if (assetManager.isLoaded(name, Music.class)) {
            return assetManager.get(name, Music.class);
        }
        return null;
    }

    /**
     * Obtem uma pele já carregada.
     * @param name Caminho da pele.
     * @return Retorna a pele ou null.
     */
    public Skin getSkin(String name) {
        if (assetManager.isLoaded(name, Skin.class)) {
            return assetManager.get(name, Skin.class);
        }
        return null;
    }

    /**
     * Obtem uma fonte já carregada.
     * @param name Caminho da fonte.
     * @return Retorna a fonte ou null.
     */
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
