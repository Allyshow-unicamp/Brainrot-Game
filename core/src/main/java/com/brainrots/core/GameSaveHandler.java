package com.brainrots.core;

import java.util.HashMap;

import com.brainrots.exception.PersistanceException;

public interface GameSaveHandler {
    public void saveProfile(PlayerProfile playerProfile) throws PersistanceException;
    public PlayerProfile loadProfile() throws PersistanceException;
    public void saveShopData(HashMap<String, Integer> itemPrices) throws PersistanceException;
    public HashMap<String, Integer> loadShopData() throws PersistanceException;
}
