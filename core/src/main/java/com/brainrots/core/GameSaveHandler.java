package com.brainrots.core;

import com.brainrots.exception.ProfilePersistanceException;

public interface GameSaveHandler {
    public void saveProfile(PlayerProfile playerProfile) throws ProfilePersistanceException;
    public PlayerProfile loadProfile() throws ProfilePersistanceException;
}
