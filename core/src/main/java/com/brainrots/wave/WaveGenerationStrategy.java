package com.brainrots.wave;

/** Interface (Strategy) para geração de dados de ondas. */
public interface WaveGenerationStrategy {

    /** @param currentWave número da wave atual (1-indexed) */
    boolean hasNextWave(int currentWave);

    /** @param currentWave número da wave atual (1-indexed) */
    WaveData generateNextWave(int currentWave);
}
