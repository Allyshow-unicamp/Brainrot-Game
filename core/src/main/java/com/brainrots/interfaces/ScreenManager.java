package com.brainrots.interfaces;

public interface ScreenManager {
    enum ScreenType{
        MAIN_MENU,
        GAMEPLAY,
        GLOBAL_SHOP,
        LEVEL_SELECTION,
        END_GAME
    }

    void switchScreen(ScreenType type);
}
