package com.brainrots.engine.mode;

import com.badlogic.gdx.utils.Array;
import com.brainrots.engine.Base;
import com.brainrots.engine.InfiniteWaveStrategy;
import com.brainrots.engine.LevelManager;
import com.brainrots.engine.WaveManager;
import com.brainrots.enemy.Enemy;

/**
 * Modo infinito (State concreto).
 *
 * Regras:
 * - Vitória: NUNCA ocorre (o objetivo é sobreviver o máximo possível)
 * - Derrota: base com HP <= 0 (mesmo critério do modo padrão)
 * - Quando todas as waves pré-definidas acabam e não há inimigos,
 *   substitui a estratégia de geração por InfiniteWaveStrategy e
 *   inicia a próxima wave proceduralmente.
 *
 * A transição de estratégia é feita automaticamente pelo evaluate(),
 * que detecta quando as waves finitas se esgotaram (currentWave >= maxWaves
 * && enemies.isEmpty()) e troca a estratégia antes de iniciar a próxima wave.
 * A flag strategySwitched garante que a troca ocorra apenas uma vez.
 */
public class InfiniteMode implements GameMode {

    private boolean strategySwitched;

    public InfiniteMode() {
        this.strategySwitched = false;
    }

    @Override
    public GameOutcome evaluate(LevelManager context, Base base, WaveManager waveManager, Array<Enemy> enemies) {
        if (!base.hasHealth()) {
            return GameOutcome.DEFEAT;
        }

        // Se as waves finitas terminaram e não há inimigos vivos,
        // troca para estratégia infinita e inicia a próxima wave.
        if (waveManager.getCurrentWave() >= waveManager.getMaxWaves() && enemies.isEmpty()) {
            if (!strategySwitched) {
                // Substitui FiniteWaveStrategy por InfiniteWaveStrategy
                waveManager.setStrategy(new InfiniteWaveStrategy(waveManager.getCurrentWave()));
                strategySwitched = true;
            }
            // Inicia a próxima wave (agora gerada pela InfiniteWaveStrategy)
            waveManager.startNextWave();
        }

        return GameOutcome.NONE;
    }

    @Override
    public void onEnter(LevelManager context) {
        // Garante que as waves comecem automaticamente.
        context.resumeWaveGeneration();
    }

    @Override
    public String getHudSuffix() {
        return " \u221E";
    }
}
