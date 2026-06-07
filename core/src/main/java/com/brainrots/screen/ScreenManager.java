package com.brainrots.screen;

public interface ScreenManager {
    enum ScreenType{
        MAIN_MENU,
        GAMEPLAY,
        GLOBAL_SHOP,
        LEVEL_SELECTION
    }

    void switchScreen(ScreenType type);
}
