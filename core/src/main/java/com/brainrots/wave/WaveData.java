package com.brainrots.wave;

import com.badlogic.gdx.utils.Array;

/** Value Object que encapsula os dados de uma onda: lista de grupos de inimigos a spawnar. */
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

    /** Grupo de inimigos do mesmo tipo a serem spawnados em uma onda. */
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
