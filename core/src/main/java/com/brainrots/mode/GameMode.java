package com.brainrots.mode;

import com.badlogic.gdx.utils.Array;
import com.brainrots.engine.Base;
import com.brainrots.engine.LevelManager;
import com.brainrots.wave.WaveManager;
import com.brainrots.enemy.Enemy;

/** Interface (State) que define as regras de vitória/derrota de um modo de jogo. */
public interface GameMode {

    /**
     * Avalia o estado atual do jogo.
     * @return VICTORY, DEFEAT ou NONE
     */
    GameOutcome evaluate(LevelManager context, Base base, WaveManager waveManager, Array<Enemy> enemies);

    /** Chamado quando o modo é ativado. */
    void onEnter(LevelManager context);

    /** Sufixo exibido no HUD ao lado do número da onda. */
    String getHudSuffix();
}
