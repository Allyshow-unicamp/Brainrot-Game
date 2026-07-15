package com.brainrots.wave;

/**
 * Interface para estratégia de geração de waves (Strategy Pattern).
 *
 * Permite que o WaveManager altere seu comportamento de geração
 * de waves sem modificar sua própria estrutura. O WaveManager
 * simplesmente delega para a estratégia a decisão de como gerar
 * a próxima wave.
 */
public interface WaveGenerationStrategy {

    /**
     * Verifica se existe uma próxima wave disponível.
     *
     * @param currentWave número da wave atual (1-indexed)
     * @return true se houver uma próxima wave para gerar
     */
    boolean hasNextWave(int currentWave);

    /**
     * Gera os dados da próxima wave.
     *
     * @param currentWave número da wave atual (1-indexed) — a implementação
     *                    deve gerar dados para a wave currentWave + 1
     * @return dados completos da wave (lista de grupos de inimigos)
     */
    WaveData generateNextWave(int currentWave);
}
