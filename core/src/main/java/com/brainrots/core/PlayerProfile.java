package com.brainrots.core;

import java.util.List;
import java.util.Map;

public class PlayerProfile {
    private int globalCoins;
    private Map<Integer, Integer> levelStars;
    private List<String> unlockedTowerIds;

    public PlayerProfile(int globalCoins, Map<Integer, Integer> levelStars, List<String> unlockedTowerIds) {
        this.globalCoins = globalCoins;
        this.levelStars = levelStars;
        this.unlockedTowerIds = unlockedTowerIds;
    }

    public int getGlobalCoins() {
        return globalCoins;
    }

    public Map<Integer, Integer> getLevelStars() {
        return levelStars;
    }

    public List<String> getUnlockedTowerIds() {
        return unlockedTowerIds;
    }

    public void addGlobalCoins(int coins) {
        this.globalCoins += coins;
    }

    public void removeGlobalCoins(int coins) {
        this.globalCoins -= coins;
    }

    public void setStars(int levelId, int stars) {
        this.levelStars.replace(levelId, stars);
    }

    public void unlockTower(String towerId) {
        this.unlockedTowerIds.add(towerId);
    }
}
