package com.brainrots.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ShopTowerDataTest {

    @Test
    void constructorSetsAllFields() {
        ShopTowerData data = new ShopTowerData("1_tung_tung", "Tung Tung", "A tower", "Shoots", 150);
        assertEquals("1_tung_tung", data.getId());
        assertEquals("Tung Tung", data.getName());
        assertEquals("A tower", data.getDescription());
        assertEquals("Shoots", data.getBehaviorDescription());
        assertEquals(150, data.getPrice());
    }

    @Test
    void defaultConstructorCreatesEmptyObject() {
        ShopTowerData data = new ShopTowerData();
        assertNull(data.getId());
        assertNull(data.getName());
        assertNull(data.getDescription());
        assertNull(data.getBehaviorDescription());
        assertEquals(0, data.getPrice());
    }

    @Test
    void gettersReturnCorrectValues() {
        ShopTowerData data = new ShopTowerData("bombardiro", "Bombardiro Crocodilo", "AOE fire", "Explodes", 300);
        assertEquals("bombardiro", data.getId());
        assertEquals(300, data.getPrice());
        assertEquals("AOE fire", data.getDescription());
    }
}
