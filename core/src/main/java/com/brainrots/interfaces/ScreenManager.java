package com.brainrots.interfaces;

/** Interface para navegação entre telas do jogo. */
public interface ScreenManager {
    enum ScreenType{
        MAIN_MENU,
        GAMEPLAY,
        GLOBAL_SHOP,
        LEVEL_SELECTION,
        END_GAME
    }

    void switchScreen(ScreenType type);

    void endProgram();
}
