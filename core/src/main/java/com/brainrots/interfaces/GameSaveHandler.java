package com.brainrots.interfaces;

import java.util.HashMap;

import com.brainrots.core.PlayerProfile;
import com.brainrots.exception.PersistanceException;

public interface GameSaveHandler {
    public void saveProfile(PlayerProfile playerProfile) throws PersistanceException;
    public PlayerProfile loadProfile() throws PersistanceException;
    public HashMap<String, Integer> loadShopData() throws PersistanceException;
    public HashMap<String, String> loadLevelData() throws PersistanceException;
}
