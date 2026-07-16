package com.brainrots.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** Gerencia assets do jogo (singleton). */
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

        /* MAPS */
        assetManager.load(Assets.MAP_67_HILLS_BG, Texture.class);
        assetManager.load(Assets.MAP_SIGMA_VOLCANO_BG, Texture.class);
        assetManager.load(Assets.MAP_WINTER_AURA_BG, Texture.class);
        assetManager.load(Assets.MAP_MINING_RIZZ_BG, Texture.class);
        assetManager.load(Assets.MAP_BETA_JUNGLE_BG, Texture.class);

        /* ENEMIES */
        assetManager.load(Assets.ENEMY_BLUE, Texture.class);
        assetManager.load(Assets.ENEMY_RED, Texture.class);
        assetManager.load(Assets.ENEMY_ORANGE, Texture.class);
        assetManager.load(Assets.ENEMY_WHITE, Texture.class);
        assetManager.load(Assets.ENEMY_GREEN, Texture.class);

        /* PROJECTILES */
        assetManager.load(Assets.PROJECTILE_BLACK, Texture.class);
        assetManager.load(Assets.PROJECTILE_FIRE, Texture.class);
        assetManager.load(Assets.PROJECTILE_WATER, Texture.class);

        /* TOWERS */
        assetManager.load(Assets.TOWER_TUNG, Texture.class);
        assetManager.load(Assets.TOWER_VACA_SATURNITA, Texture.class);
        assetManager.load(Assets.TOWER_TRIPPI_TROPPI, Texture.class);
        assetManager.load(Assets.TOWER_TRALALERO_TRALALA, Texture.class);
        assetManager.load(Assets.TOWER_PATAPIM, Texture.class);
        assetManager.load(Assets.TOWER_NUCLEARO_DINOSAURO, Texture.class);
        assetManager.load(Assets.TOWER_LIRILI_LARILA, Texture.class);
        assetManager.load(Assets.TOWER_CAPPUCCINO, Texture.class);
        assetManager.load(Assets.TOWER_CAPPUCCINA, Texture.class);
        assetManager.load(Assets.TOWER_BOMBARDILO_CROCODILO, Texture.class);

        /*Synergy */
        assetManager.load(Assets.TOWER_BALLERINA, Texture.class);
        assetManager.load(Assets.TUNG_SYNERGY,Texture.class);
        assetManager.load(Assets.NUCLEARO_SYNERGY,Texture.class);
        assetManager.load(Assets.CAPPUCCINO_SYNERGY,Texture.class);
        assetManager.load(Assets.BOMBARDILO_SYNERGY,Texture.class);

        /* SOUNDS */
        assetManager.load(Assets.MUSIC_GAME, Music.class);

        /* HUD */
        assetManager.load(Assets.PLAY_BUTTON, Texture.class);
        assetManager.load(Assets.PAUSE_BUTTON, Texture.class);
        assetManager.load(Assets.FOWARD_BUTTON_1, Texture.class);
        assetManager.load(Assets.FOWARD_BUTTON_2, Texture.class);
        assetManager.load(Assets.HOME_BUTTON, Texture.class);
        assetManager.load(Assets.UI_SKIN, Skin.class);
        assetManager.load(Assets.UI_FONT, BitmapFont.class);
        assetManager.load(Assets.NORMAL_COIN,Texture.class);
        assetManager.load(Assets.DEFEAT_SCREEN, Texture.class);
        assetManager.load(Assets.VICTORY_SCREEN, Texture.class);
        assetManager.load(Assets.MENU_SCREEN, Texture.class);
        assetManager.load(Assets.SELECTION_SCREEN, Texture.class);

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

    public BitmapFont createFont(String name, float scale) {
        BitmapFont font = new BitmapFont(Gdx.files.internal(name));
        font.getData().setScale(scale);
        return font;
    }

    public void dispose() {
        assetManager.dispose();
    }
}
