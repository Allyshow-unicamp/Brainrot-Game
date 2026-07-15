package com.brainrots.wave;

import com.badlogic.gdx.utils.Array;

/**
 * Encapsula os dados de uma wave: lista de grupos de inimigos a spawnar.
 *
 * Usado como valor de retorno (Value Object) das estratégias de geração
 * de waves (WaveGenerationStrategy). Desacopla o WaveManager do formato
 * de origem dos dados (JSON para FiniteWaveStrategy, geração procedural
 * para InfiniteWaveStrategy).
 *
 * SpawnGroup é uma classe interna pública que representa um tipo de
 * inimigo com sua quantidade, intervalo entre spawns e delay inicial.
 */
public class WaveData {

    private Array<SpawnGroup> groups;

    public WaveData() {
        this.groups = new Array<>();
    }

    public WaveData(Array<SpawnGroup> groups) {
        this.groups = groups;
    }

    public void addGroup(String type, int count, float interval, int delay) {
        groups.add(new SpawnGroup(type, count, interval, delay));
    }

    public Array<SpawnGroup> getGroups() {
        return groups;
    }

    /**
     * Representa um grupo de inimigos do mesmo tipo a serem spawnados
     * em uma wave.
     */
    public static class SpawnGroup {
        public final String type;
        public final int count;
        public final float spawnInterval;
        public final int spawnTime;

        public SpawnGroup(String type, int count, float spawnInterval, int spawnTime) {
            this.type = type;
            this.count = count;
            this.spawnInterval = spawnInterval;
            this.spawnTime = spawnTime;
        }
    }
}
