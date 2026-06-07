package com.brainrots.core;

public interface GameSaveHandler {
    public void saveProfile(PlayerProfile playerProfile);
    public PlayerProfile loadProfile();
}
