package com.brainrots.core;

import java.util.Map;

import com.brainrots.exception.PersistanceException;

public interface GameSaveHandler {
    public void saveProfile(PlayerProfile playerProfile) throws PersistanceException;
    public PlayerProfile loadProfile() throws PersistanceException;
    public void saveShopData(Map<String, Integer> itemPrices) throws PersistanceException;
    public Map<String, Integer> loadShopData() throws PersistanceException;
}
