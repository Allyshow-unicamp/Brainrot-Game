package com.brainrots.mode;

import com.badlogic.gdx.utils.Array;
import com.brainrots.engine.Base;
import com.brainrots.engine.LevelManager;
import com.brainrots.wave.WaveManager;
import com.brainrots.enemy.Enemy;

/**
 * Modo de jogo padrão (State concreto).
 *
 * Regras:
 * - Vitória: base com HP > 0, wave atual >= max waves, nenhum inimigo vivo
 * - Derrota: base com HP <= 0
 *
 * Comportamento idêntico ao jogo original (BrainRotGame v1).
 * Este modo é o default do LevelManager.
 */
public class StandardMode implements GameMode {

    @Override
    public GameOutcome evaluate(LevelManager context, Base base, WaveManager waveManager, Array<Enemy> enemies) {
        if (!base.hasHealth()) {
            return GameOutcome.DEFEAT;
        }
        if (waveManager.getCurrentWave() >= waveManager.getMaxWaves() && enemies.isEmpty()) {
            return GameOutcome.VICTORY;
        }
        return GameOutcome.NONE;
    }

    @Override
    public void onEnter(LevelManager context) {
        // Modo padrão não requer configuração especial.
    }

    @Override
    public String getHudSuffix() {
        return "";
    }
}
