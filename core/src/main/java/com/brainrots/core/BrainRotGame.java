package com.brainrots.core;

import java.nio.file.Paths;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brainrots.screen.GameplayScreen;
import com.brainrots.screen.GlobalShopScreen;
import com.brainrots.screen.LevelSelectionScreen;
import com.brainrots.screen.MainMenuScreen;
import com.brainrots.screen.ScreenManager;

/**
 * Classe que gerencia todo o jogo.
 */
public class BrainRotGame extends Game implements ScreenManager {

    private SpriteBatch batch;
    private GameResourceManager resourceManager;
    private ShapeRenderer shapeRenderer;
    private PlayerProfile playerProfile;
    private GameSaveHandler gameSaveHandler;

    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    @Override
    public void create() {
        String profilePath = Paths.get("data", "player-data.json").toString();

        gameSaveHandler = new JsonGameSaveHandler(profilePath, "");
        try {
            this.playerProfile = gameSaveHandler.loadProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        resourceManager = GameResourceManager.getGameResourceManager();
        resourceManager.loadAssets();

        Music backgroundMusic = resourceManager.getSound(Assets.MUSIC_GAME);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        switchScreen(ScreenType.MAIN_MENU);
    }

    public void switchScreen(ScreenType type) {
        switch (type) {
            case MAIN_MENU:
                setScreen(new MainMenuScreen(batch, resourceManager, this, shapeRenderer));
                break;
            case GAMEPLAY:
                setScreen(new GameplayScreen(batch, resourceManager, this, shapeRenderer));
                break;
            case GLOBAL_SHOP:
                setScreen(new GlobalShopScreen(batch, resourceManager, this, shapeRenderer));
                break;
            case LEVEL_SELECTION:
                setScreen(new LevelSelectionScreen(batch, resourceManager, this, shapeRenderer));
                break;
            default:
                break;
        }
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        resourceManager.dispose();
        batch.dispose();

        try {
            gameSaveHandler.saveProfile(playerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
