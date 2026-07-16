package com.brainrots.wave;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;

import org.junit.jupiter.api.Test;

class WaveManagerTest {

    private WaveManager createWaveManager(int maxWaves, WaveGenerationStrategy strategy) {
        Array<Vector2> emptyPath = new Array<>();
        return new WaveManager(emptyPath, strategy, maxWaves);
    }

    private WaveGenerationStrategy noMoreWavesStrategy() {
        return new WaveGenerationStrategy() {
            @Override
            public boolean hasNextWave(int cw) { return false; }
            @Override
            public WaveData generateNextWave(int cw) { return new WaveData(); }
        };
    }

    private WaveGenerationStrategy fixedWavesStrategy(int max) {
        return new WaveGenerationStrategy() {
            @Override
            public boolean hasNextWave(int cw) { return cw < max; }
            @Override
            public WaveData generateNextWave(int cw) { return new WaveData(); }
        };
    }

    @Test
    void startsAtWaveZero() {
        WaveManager wm = createWaveManager(5, noMoreWavesStrategy());
        assertEquals(0, wm.getCurrentWave());
    }

    @Test
    void startsWithMaxWaves() {
        WaveManager wm = createWaveManager(10, noMoreWavesStrategy());
        assertEquals(10, wm.getMaxWaves());
    }

    @Test
    void startsNotActiveAndPaused() {
        WaveManager wm = createWaveManager(5, noMoreWavesStrategy());
        assertFalse(wm.isWaveActive());
        assertTrue(wm.isPausedWave());
    }

    @Test
    void startNextWaveIncrementsCurrentWave() {
        WaveManager wm = createWaveManager(5, fixedWavesStrategy(5));
        wm.startNextWave();
        assertEquals(1, wm.getCurrentWave());
    }

    @Test
    void startNextWaveSetsWaveActive() {
        WaveManager wm = createWaveManager(5, fixedWavesStrategy(5));
        wm.startNextWave();
        assertTrue(wm.isWaveActive());
    }

    @Test
    void startNextWaveDoesNotExceedMaxWaves() {
        WaveManager wm = createWaveManager(2, fixedWavesStrategy(2));
        wm.startNextWave();
        wm.startNextWave();
        wm.startNextWave();
        assertEquals(2, wm.getCurrentWave());
    }

    @Test
    void startNextWaveWithNoMoreWavesDoesNothing() {
        WaveManager wm = createWaveManager(5, noMoreWavesStrategy());
        wm.startNextWave();
        assertEquals(0, wm.getCurrentWave());
        assertFalse(wm.isWaveActive());
    }

    @Test
    void pauseWaveGenerationSetsPaused() {
        WaveManager wm = createWaveManager(5, fixedWavesStrategy(5));
        wm.resumeWaveGeneration();
        assertFalse(wm.isPausedWave());
        wm.pauseWaveGeneration();
        assertTrue(wm.isPausedWave());
    }

    @Test
    void resumeWaveGenerationResumes() {
        WaveManager wm = createWaveManager(5, fixedWavesStrategy(5));
        wm.resumeWaveGeneration();
        assertFalse(wm.isPausedWave());
    }

    @Test
    void setStrategyChangesStrategy() {
        WaveManager wm = createWaveManager(5, noMoreWavesStrategy());
        wm.setStrategy(fixedWavesStrategy(5));
        wm.startNextWave();
        assertEquals(1, wm.getCurrentWave());
    }

    @Test
    void updateAutoStartsNextWaveWhenNotPaused() {
        WaveManager wm = createWaveManager(5, fixedWavesStrategy(5));
        wm.resumeWaveGeneration();
        Array<Enemy> enemies = new Array<>();
        wm.update(1.0f, enemies);
        assertEquals(1, wm.getCurrentWave());
    }

    @Test
    void updateDoesNotAutoStartWhenPaused() {
        WaveManager wm = createWaveManager(5, fixedWavesStrategy(5));
        Array<Enemy> enemies = new Array<>();
        wm.update(1.0f, enemies);
        assertEquals(0, wm.getCurrentWave());
    }

    @Test
    void updateSetsWaveInactiveWhenSpawnersAndEnemiesEmpty() {
        WaveManager wm = createWaveManager(5, fixedWavesStrategy(5));
        wm.startNextWave();
        assertTrue(wm.isWaveActive());
        Array<Enemy> enemies = new Array<>();
        wm.update(1.0f, enemies);
        assertFalse(wm.isWaveActive());
    }

    @Test
    void waveWithNoGroupsCompletesImmediately() {
        WaveData waveData = new WaveData();
        WaveGenerationStrategy strategy = new WaveGenerationStrategy() {
            @Override
            public boolean hasNextWave(int cw) { return cw < 1; }
            @Override
            public WaveData generateNextWave(int cw) { return waveData; }
        };

        WaveManager wm = createWaveManager(1, strategy);
        wm.resumeWaveGeneration();
        Array<Enemy> enemies = new Array<>();
        wm.update(0.1f, enemies);
        wm.update(0.1f, enemies);
        assertFalse(wm.isWaveActive());
        assertTrue(wm.isPausedWave());
    }

    @Test
    void waveWithSpawnGroupsCreatesSpawners() {
        WaveData waveData = new WaveData();
        waveData.addGroup("red", 3, 1.0f, 0);
        waveData.addGroup("blue", 2, 1.5f, 2);

        WaveGenerationStrategy strategy = new WaveGenerationStrategy() {
            @Override
            public boolean hasNextWave(int cw) { return cw < 1; }
            @Override
            public WaveData generateNextWave(int cw) { return waveData; }
        };

        WaveManager wm = createWaveManager(1, strategy);
        wm.startNextWave();
        assertTrue(wm.isWaveActive());
    }

    @Test
    void updateWithWaveActiveAndSpawnersStillActive() {
        WaveData waveData = new WaveData();
        waveData.addGroup("red", 5, 5.0f, 0);

        WaveGenerationStrategy strategy = new WaveGenerationStrategy() {
            @Override
            public boolean hasNextWave(int cw) { return cw < 1; }
            @Override
            public WaveData generateNextWave(int cw) { return waveData; }
        };

        WaveManager wm = createWaveManager(1, strategy);
        wm.resumeWaveGeneration();
        Array<Enemy> enemies = new Array<>();
        wm.update(1.0f, enemies);
        assertTrue(wm.isWaveActive());
    }
}
