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

        Music backgroundMusic = resourceManager.getSound("Soundtracks/game_song.mp3");
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        switchScreen(ScreenType.MAIN_MENU);
    }

    public void switchScreen(ScreenType type){
        switch (type) {
            case MAIN_MENU:
                this.setScreen(new MainMenuScreen(batch, resourceManager, this));
                break;
            case GAMEPLAY:
                this.setScreen(new GameplayScreen(batch, resourceManager, this));
            default:
                break;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
