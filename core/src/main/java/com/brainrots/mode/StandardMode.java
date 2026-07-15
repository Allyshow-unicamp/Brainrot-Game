package com.brainrots.mode;

import com.badlogic.gdx.utils.Array;
import com.brainrots.engine.Base;
import com.brainrots.engine.LevelManager;
import com.brainrots.wave.WaveManager;
import com.brainrots.enemy.Enemy;

/** Modo padrão: vitória ao limpar todas as ondas com base intacta, derrota se a base for destruída. */
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
    }

    @Override
    public String getHudSuffix() {
        return "";
    }
}
