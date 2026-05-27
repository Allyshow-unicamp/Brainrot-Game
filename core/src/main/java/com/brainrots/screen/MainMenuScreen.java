package com.brainrots.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brainrots.core.GameResourceManager;
import com.brainrots.screen.ScreenManager.ScreenType;

public class MainMenuScreen extends GameScreen {

    public MainMenuScreen(SpriteBatch batch, GameResourceManager resourceManager, ScreenManager screenManager){
        super(batch, resourceManager, screenManager);
    }

    @Override
    protected void drawScreen(float delta) {
        screenManager.SwitchScreen(ScreenType.GAMEPLAY);

    }
}
