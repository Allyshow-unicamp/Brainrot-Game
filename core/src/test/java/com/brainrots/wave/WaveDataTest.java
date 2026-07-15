package com.brainrots.wave;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.utils.Array;

import org.junit.jupiter.api.Test;

class WaveDataTest {

    @Test
    void defaultConstructorCreatesEmptyGroups() {
        WaveData data = new WaveData();
        assertNotNull(data.getGroups());
        assertTrue(data.getGroups().isEmpty());
    }

    @Test
    void addGroupAddsToGroups() {
        WaveData data = new WaveData();
        data.addGroup("red", 5, 1.0f, 0);
        assertEquals(1, data.getGroups().size);
    }

    @Test
    void addGroupCreatesCorrectSpawnGroup() {
        WaveData data = new WaveData();
        data.addGroup("blue", 3, 2.0f, 5);
        WaveData.SpawnGroup group = data.getGroups().first();
        assertEquals("blue", group.type);
        assertEquals(3, group.count);
        assertEquals(2.0f, group.spawnInterval, 0.001f);
        assertEquals(5, group.spawnTime);
    }

    @Test
    void addMultipleGroups() {
        WaveData data = new WaveData();
        data.addGroup("red", 5, 1.0f, 0);
        data.addGroup("blue", 3, 2.0f, 5);
        data.addGroup("green", 10, 0.5f, 10);
        assertEquals(3, data.getGroups().size);
    }

    @Test
    void constructorWithGroups() {
        Array<WaveData.SpawnGroup> groups = new Array<>();
        groups.add(new WaveData.SpawnGroup("red", 5, 1.0f, 0));
        groups.add(new WaveData.SpawnGroup("blue", 3, 2.0f, 5));

        WaveData data = new WaveData(groups);
        assertEquals(2, data.getGroups().size);
        assertEquals("red", data.getGroups().get(0).type);
        assertEquals("blue", data.getGroups().get(1).type);
    }

    @Test
    void spawnGroupFieldsArePublic() {
        WaveData.SpawnGroup group = new WaveData.SpawnGroup("orange", 7, 1.5f, 3);
        assertEquals("orange", group.type);
        assertEquals(7, group.count);
        assertEquals(1.5f, group.spawnInterval, 0.001f);
        assertEquals(3, group.spawnTime);
    }

    @Test
    void emptyWaveDataHasNoGroups() {
        WaveData data = new WaveData();
        assertEquals(0, data.getGroups().size);
    }
}
