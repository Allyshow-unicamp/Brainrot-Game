package com.brainrots.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brainrots.screen.GameplayScreen;
import com.brainrots.screen.MainMenuScreen;
import com.brainrots.screen.ScreenManager;

public class BrainRotGame extends Game implements ScreenManager {

    private SpriteBatch batch;
    private GameResourceManager resourceManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        resourceManager = new GameResourceManager();
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
                setScreen(new MainMenuScreen(batch, resourceManager, this));
                break;
            case GAMEPLAY:
                setScreen(new GameplayScreen(batch, resourceManager, this));
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
    }
}
