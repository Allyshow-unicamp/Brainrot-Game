package com.brainrots;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brainrots.ScreenManager.ScreenType;

public class MainMenuScreen extends GameScreen {

    public MainMenuScreen(SpriteBatch batch, GameResourceManager resourceManager, ScreenManager screenManager){
        super(batch, resourceManager, screenManager);
    }

    @Override
    protected void drawScreen(float delta) {
        screenManager.SwitchScreen(ScreenType.GAMEPLAY);
        
    }
}
