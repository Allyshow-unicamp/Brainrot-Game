package com.brainrots.wave;

import com.badlogic.gdx.math.MathUtils;

/**
 * Estratégia de waves infinitas: gera waves proceduralmente com
 * dificuldade progressiva.
 *
 * Características:
 * - hasNextWave() sempre retorna true (nunca acaba)
 * - A dificuldade escala linearmente com o número da wave:
 *   - Quantidade de inimigos aumenta 10% por wave acima da base
 *   - Tipos sorteados aleatoriamente entre os disponíveis
 * - Mini-bosses (6_7_Ball) a cada BOSS_INTERVAL waves
 *
 * Nota: o escalonamento real de HP dos inimigos é aplicado pelo
 * EnemyFactory em futura implementação. Por enquanto, usa os
 * templates existentes com HP fixo.
 */
public class InfiniteWaveStrategy implements WaveGenerationStrategy {

    private static final float COUNT_SCALE = 0.10f;
    private static final int BOSS_INTERVAL = 5;

    private static final String[] ENEMY_TYPES = {"red", "green", "blue"};
    private static final String[] BOSS_TYPES = {"white", "orange"};

    private int baseWave;

    /**
     * @param baseWave número da wave a partir da qual o modo infinito
     *                 começou. Usado para calcular o escalonamento:
     *                 uma wave com currentWave=10 começando em baseWave=5
     *                 terá menos escala que uma começando em baseWave=1.
     */
    public InfiniteWaveStrategy(int baseWave) {
        this.baseWave = baseWave;
    }

    @Override
    public boolean hasNextWave(int currentWave) {
        return true;
    }

    @Override
    public WaveData generateNextWave(int currentWave) {
        WaveData data = new WaveData();
        int waveOffset = currentWave - baseWave; // offset dentro do modo infinito

        int groupCount = MathUtils.random(2, 4);
        for (int i = 0; i < groupCount; i++) {
            String type = ENEMY_TYPES[MathUtils.random(ENEMY_TYPES.length - 1)];
            int baseCount = 5 + waveOffset;
            int count = Math.round(baseCount * (1f + COUNT_SCALE * waveOffset));
            float interval = Math.max(0.2f, 0.5f - waveOffset * 0.01f);
            int delay = i * 3;

            data.addGroup(type, count, interval, delay);
        }

        if (waveOffset > 0 && waveOffset % BOSS_INTERVAL == 0) {
            String bossType = BOSS_TYPES[MathUtils.random(BOSS_TYPES.length - 1)];
            int bossCount = 1 + waveOffset / BOSS_INTERVAL;
            data.addGroup(bossType, bossCount, 2.0f, 10);
        }

        return data;
    }
}
