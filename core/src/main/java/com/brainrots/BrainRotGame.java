package com.brainrots;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BrainRotGame extends Game implements ScreenManager {
    
    private SpriteBatch batch;
    private GameResourceManager resourceManager;

    @Override
    public void create() {
        batch = new SpriteBatch();
        resourceManager = new GameResourceManager();

        this.setScreen(new MainMenuScreen(batch, resourceManager, this));
    }

    public void SwitchScreen(ScreenType type){
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

}
