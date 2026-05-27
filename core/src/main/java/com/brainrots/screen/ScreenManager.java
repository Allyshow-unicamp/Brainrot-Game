package com.brainrots.screen;

public interface ScreenManager {
    enum ScreenType{
        MAIN_MENU,
        GAMEPLAY
    }

    void SwitchScreen(ScreenType type);
}
