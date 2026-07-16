package com.brainrots.enemy;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EnemySpawnerTest {

    @Test
    void initialStateIsNotEmpty() {
        EnemySpawner spawner = new EnemySpawner("red", 5, 1.0f, 0);
        assertFalse(spawner.isEmpty());
        assertEquals(5, spawner.getCount());
    }

    @Test
    void isEmptyWhenCountIsZero() {
        EnemySpawner spawner = new EnemySpawner("red", 0, 1.0f, 0);
        assertTrue(spawner.isEmpty());
    }

    @Test
    void shouldSpawnReturnsFalseBeforeSpawnTime() {
        EnemySpawner spawner = new EnemySpawner("red", 3, 1.0f, 5);
        assertFalse(spawner.shouldSpawn(1.0f));
        assertFalse(spawner.shouldSpawn(1.0f));
        assertFalse(spawner.shouldSpawn(1.0f));
    }

    @Test
    void shouldSpawnReturnsTrueAfterSpawnTime() {
        EnemySpawner spawner = new EnemySpawner("red", 3, 1.0f, 2);
        assertFalse(spawner.shouldSpawn(1.0f));
        assertFalse(spawner.shouldSpawn(0.5f));
        assertTrue(spawner.shouldSpawn(0.6f));
    }

    @Test
    void shouldSpawnDecrementsCount() {
        EnemySpawner spawner = new EnemySpawner("red", 3, 1.0f, 0);
        assertTrue(spawner.shouldSpawn(1.0f));
        assertEquals(2, spawner.getCount());
        assertTrue(spawner.shouldSpawn(1.0f));
        assertEquals(1, spawner.getCount());
        assertTrue(spawner.shouldSpawn(1.0f));
        assertEquals(0, spawner.getCount());
    }

    @Test
    void shouldSpawnReturnsFalseBetweenSpawns() {
        EnemySpawner spawner = new EnemySpawner("red", 3, 2.0f, 0);
        assertTrue(spawner.shouldSpawn(2.0f));
        assertFalse(spawner.shouldSpawn(0.5f));
        assertTrue(spawner.shouldSpawn(1.6f));
    }

    @Test
    void isEmptyAfterAllSpawned() {
        EnemySpawner spawner = new EnemySpawner("red", 2, 1.0f, 0);
        spawner.shouldSpawn(1.0f);
        spawner.shouldSpawn(1.0f);
        assertTrue(spawner.isEmpty());
    }

    @Test
    void getTypeReturnsCorrectType() {
        EnemySpawner spawner = new EnemySpawner("blue", 5, 1.0f, 0);
        assertEquals("blue", spawner.getType());
    }

    @Test
    void shouldSpawnWithZeroIntervalSpawnsImmediately() {
        EnemySpawner spawner = new EnemySpawner("red", 3, 0f, 0);
        assertTrue(spawner.shouldSpawn(0.0f));
        assertEquals(2, spawner.getCount());
    }

    @Test
    void timerResetsAfterEachSpawn() {
        EnemySpawner spawner = new EnemySpawner("red", 3, 1.0f, 0);
        spawner.shouldSpawn(1.0f);
        assertFalse(spawner.shouldSpawn(0.5f));
        assertTrue(spawner.shouldSpawn(0.6f));
    }

    @Test
    void shouldSpawnNotTriggerBeforeSpawnTimeEvenWithLargeDelta() {
        EnemySpawner spawner = new EnemySpawner("red", 3, 0.5f, 10);
        assertFalse(spawner.shouldSpawn(9.0f));
        assertTrue(spawner.shouldSpawn(1.0f));
    }
}
