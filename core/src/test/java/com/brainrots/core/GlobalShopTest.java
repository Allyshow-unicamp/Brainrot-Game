package com.brainrots.core;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.brainrots.exception.InvalidGameActionException;
import com.brainrots.exception.PersistanceException;
import com.brainrots.interfaces.GameSaveHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GlobalShopTest {

    private PlayerProfile profile;

    @BeforeEach
    void setUp() {
        List<String> unlocked = new ArrayList<>();
        unlocked.add("1_tung_tung");
        profile = new PlayerProfile(1000, new HashMap<>(), unlocked);
    }

    private GlobalShop createShopWithTowers(HashMap<String, ShopTowerData> data) {
        GameSaveHandler handler = new GameSaveHandler() {
            @Override
            public void saveProfile(PlayerProfile p) throws PersistanceException {}
            @Override
            public PlayerProfile loadProfile() throws PersistanceException { return null; }
            @Override
            public HashMap<String, ShopTowerData> loadShopData() throws PersistanceException { return data; }
            @Override
            public HashMap<String, String> loadLevelData() throws PersistanceException { return null; }
        };
        return new GlobalShop(handler);
    }

    @Test
    void purchaseTowerUnlocksAndDeductsCoins() throws InvalidGameActionException {
        HashMap<String, ShopTowerData> data = new HashMap<>();
        data.put("bombardiro", new ShopTowerData("bombardiro", "Bombardiro", "desc", "beh", 300));
        GlobalShop shop = createShopWithTowers(data);

        shop.purchaseTower(profile, "bombardiro");
        assertTrue(profile.isTowerUnlocked("bombardiro"));
        assertEquals(700, profile.getGlobalCoins());
    }

    @Test
    void purchaseTowerThrowsWhenNotFound() {
        HashMap<String, ShopTowerData> data = new HashMap<>();
        GlobalShop shop = createShopWithTowers(data);

        InvalidGameActionException ex = assertThrows(InvalidGameActionException.class,
            () -> shop.purchaseTower(profile, "nonexistent"));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void purchaseTowerThrowsWhenAlreadyOwned() {
        HashMap<String, ShopTowerData> data = new HashMap<>();
        data.put("1_tung_tung", new ShopTowerData("1_tung_tung", "Tung", "desc", "beh", 150));
        GlobalShop shop = createShopWithTowers(data);

        InvalidGameActionException ex = assertThrows(InvalidGameActionException.class,
            () -> shop.purchaseTower(profile, "1_tung_tung"));
        assertTrue(ex.getMessage().contains("already own"));
    }

    @Test
    void purchaseTowerThrowsWhenNotEnoughCoins() {
        HashMap<String, ShopTowerData> data = new HashMap<>();
        data.put("expensive", new ShopTowerData("expensive", "Expensive", "desc", "beh", 9999));
        GlobalShop shop = createShopWithTowers(data);

        InvalidGameActionException ex = assertThrows(InvalidGameActionException.class,
            () -> shop.purchaseTower(profile, "expensive"));
        assertTrue(ex.getMessage().contains("Not enough coins"));
    }

    @Test
    void purchaseMultipleTowers() throws InvalidGameActionException {
        HashMap<String, ShopTowerData> data = new HashMap<>();
        data.put("tower_a", new ShopTowerData("tower_a", "A", "desc", "beh", 200));
        data.put("tower_b", new ShopTowerData("tower_b", "B", "desc", "beh", 300));
        GlobalShop shop = createShopWithTowers(data);

        shop.purchaseTower(profile, "tower_a");
        shop.purchaseTower(profile, "tower_b");
        assertTrue(profile.isTowerUnlocked("tower_a"));
        assertTrue(profile.isTowerUnlocked("tower_b"));
        assertEquals(500, profile.getGlobalCoins());
    }

    @Test
    void getShopDataReturnsData() {
        HashMap<String, ShopTowerData> data = new HashMap<>();
        data.put("test", new ShopTowerData("test", "Test", "desc", "beh", 100));
        GlobalShop shop = createShopWithTowers(data);
        assertEquals(data, shop.getShopData());
    }

    @Test
    void purchaseExactCoinsWorks() throws InvalidGameActionException {
        List<String> emptyUnlocked = new ArrayList<>();
        PlayerProfile poorProfile = new PlayerProfile(150, new HashMap<>(), emptyUnlocked);
        HashMap<String, ShopTowerData> data = new HashMap<>();
        data.put("tower", new ShopTowerData("tower", "Tower", "desc", "beh", 150));
        GlobalShop shop = createShopWithTowers(data);

        shop.purchaseTower(poorProfile, "tower");
        assertTrue(poorProfile.isTowerUnlocked("tower"));
        assertEquals(0, poorProfile.getGlobalCoins());
    }
}
