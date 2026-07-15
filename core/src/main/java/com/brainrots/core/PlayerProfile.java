package com.brainrots.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Dados do jogador: moedas globais, estrelas por nível e torres desbloqueadas. */
public class PlayerProfile {
    private int globalCoins;
    private HashMap<String, Integer> levelStars;
    private List<String> unlockedTowerIds;

    public PlayerProfile() {
        this.globalCoins = 0;
        this.levelStars = new HashMap<>();
        this.unlockedTowerIds = new ArrayList<>();
    }

    public PlayerProfile(int globalCoins, HashMap<String, Integer> levelStars, List<String> unlockedTowerIds) {
        this.globalCoins = globalCoins;
        this.levelStars = levelStars;
        this.unlockedTowerIds = unlockedTowerIds;
    }

    public int getGlobalCoins() {
        return globalCoins;
    }

    public HashMap<String, Integer> getLevelStars() {
        return levelStars;
    }

    public List<String> getUnlockedTowerIds() {
        if (unlockedTowerIds == null) {
            unlockedTowerIds = new ArrayList<>();
            unlockedTowerIds.add("tung_tung");
        }
        return unlockedTowerIds;
    }

    public void addGlobalCoins(int coins) {
        this.globalCoins += coins;
    }

    public void removeGlobalCoins(int coins) {
        this.globalCoins -= coins;
    }

    public void setStars(String levelId, int stars) {
        this.levelStars.put(levelId, stars);
    }

    public void unlockTower(String towerId) {
        if (!unlockedTowerIds.contains(towerId)) {
            this.unlockedTowerIds.add(towerId);
        }
    }

    public boolean isTowerUnlocked(String towerId) {
        return getUnlockedTowerIds().contains(towerId);
    }
}
