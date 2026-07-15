package com.brainrots.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerProfileTest {

    private PlayerProfile profile;

    @BeforeEach
    void setUp() {
        profile = new PlayerProfile();
    }

    @Test
    void startsWithZeroCoins() {
        assertEquals(0, profile.getGlobalCoins());
    }

    @Test
    void constructorWithCoins() {
        PlayerProfile p = new PlayerProfile(500, new HashMap<>(), new ArrayList<>());
        assertEquals(500, p.getGlobalCoins());
    }

    @Test
    void addGlobalCoinsIncreasesBalance() {
        profile.addGlobalCoins(100);
        assertEquals(100, profile.getGlobalCoins());
    }

    @Test
    void removeGlobalCoinsDecreasesBalance() {
        profile.addGlobalCoins(200);
        profile.removeGlobalCoins(80);
        assertEquals(120, profile.getGlobalCoins());
    }

    @Test
    void removeGlobalCoinsCanGoNegative() {
        profile.removeGlobalCoins(50);
        assertEquals(-50, profile.getGlobalCoins());
    }

    @Test
    void multipleCoinOperationsMaintainBalance() {
        profile.addGlobalCoins(300);
        profile.removeGlobalCoins(100);
        profile.addGlobalCoins(50);
        assertEquals(250, profile.getGlobalCoins());
    }

    @Test
    void setStarsStoresCorrectly() {
        profile.setStars("67Hills", 3);
        assertEquals(3, profile.getLevelStars().get("67Hills"));
    }

    @Test
    void setStarsOverwritesExisting() {
        profile.setStars("67Hills", 1);
        profile.setStars("67Hills", 3);
        assertEquals(3, profile.getLevelStars().get("67Hills"));
    }

    @Test
    void setStarsMultipleLevels() {
        profile.setStars("67Hills", 3);
        profile.setStars("WinterAura", 2);
        assertEquals(3, profile.getLevelStars().get("67Hills"));
        assertEquals(2, profile.getLevelStars().get("WinterAura"));
    }

    @Test
    void getLevelStarsReturnsMap() {
        assertNotNull(profile.getLevelStars());
        assertTrue(profile.getLevelStars().isEmpty());
    }

    @Test
    void unlockTowerAddsTowerId() {
        profile.unlockTower("bombardiro_crocodilo");
        assertTrue(profile.isTowerUnlocked("bombardiro_crocodilo"));
    }

    @Test
    void unlockTowerDoesNotDuplicate() {
        profile.unlockTower("bombardiro_crocodilo");
        profile.unlockTower("bombardiro_crocodilo");
        assertEquals(1, profile.getUnlockedTowerIds().size());
    }

    @Test
    void isTowerUnlockedReturnsFalseWhenNotUnlocked() {
        assertFalse(profile.isTowerUnlocked("nuclearo_dinosauro"));
    }

    @Test
    void isTowerUnlockedReturnsTrueWhenUnlocked() {
        profile.unlockTower("nuclearo_dinosauro");
        assertTrue(profile.isTowerUnlocked("nuclearo_dinosauro"));
    }

    @Test
    void getUnlockedTowerIdsReturnsDefaultWhenNull() {
        PlayerProfile p = new PlayerProfile(0, new HashMap<>(), null);
        List<String> ids = p.getUnlockedTowerIds();
        assertNotNull(ids);
        assertEquals(1, ids.size());
        assertEquals("tung_tung", ids.get(0));
    }

    @Test
    void getUnlockedTowerIdsReturnsExistingList() {
        profile.unlockTower("test_tower");
        List<String> ids = profile.getUnlockedTowerIds();
        assertTrue(ids.contains("test_tower"));
    }

    @Test
    void multipleTowersUnlocked() {
        profile.unlockTower("tower_a");
        profile.unlockTower("tower_b");
        profile.unlockTower("tower_c");
        assertTrue(profile.isTowerUnlocked("tower_a"));
        assertTrue(profile.isTowerUnlocked("tower_b"));
        assertTrue(profile.isTowerUnlocked("tower_c"));
        assertFalse(profile.isTowerUnlocked("tower_d"));
    }

    @Test
    void constructorWithAllParams() {
        HashMap<String, Integer> stars = new HashMap<>();
        stars.put("67Hills", 3);
        List<String> unlocked = new ArrayList<>();
        unlocked.add("tung_tung");
        unlocked.add("bombardiro_crocodilo");

        PlayerProfile p = new PlayerProfile(1000, stars, unlocked);
        assertEquals(1000, p.getGlobalCoins());
        assertEquals(3, p.getLevelStars().get("67Hills"));
        assertTrue(p.isTowerUnlocked("tung_tung"));
        assertTrue(p.isTowerUnlocked("bombardiro_crocodilo"));
    }
}
