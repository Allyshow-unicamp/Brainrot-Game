package com.brainrots.screen;

public interface ScreenManager {
    enum ScreenType{
        MAIN_MENU,
        GAMEPLAY
    }

    void switchScreen(ScreenType type);
}
