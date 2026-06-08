package com.brainrots.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.brainrots.exception.PersistanceException;

public class JsonGameSaveHandler implements GameSaveHandler {
    private String profileSaveFileName;
    private String shopSaveFileName;
    private String levelSaveFileName;

    public JsonGameSaveHandler(String profileSaveFileName, String shopSaveFileName, String levelSaveFileName) {
        this.profileSaveFileName = profileSaveFileName;
        this.shopSaveFileName = shopSaveFileName;
        this.levelSaveFileName = levelSaveFileName;
    }

    @Override
    public void saveProfile(PlayerProfile playerProfile) {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(playerProfile);

        FileHandle file = Gdx.files.local(profileSaveFileName);
        file.writeString(jsonString, false);
    }

    @Override
    public PlayerProfile loadProfile() {
        FileHandle file = Gdx.files.local(profileSaveFileName);

        if (file.exists() && file != null) {
            Json json = new Json();
            return json.fromJson(PlayerProfile.class, file);
        }

        HashMap<Integer, Integer> defaultMap = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            defaultMap.put(i, 0);
        }

        return new PlayerProfile(0, defaultMap, new ArrayList<>());
    }

    private void saveShopData(HashMap<String, Integer> itemPrices) throws PersistanceException {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(itemPrices);

        FileHandle file = Gdx.files.local(shopSaveFileName);
        file.writeString(jsonString, false);
    }

    @Override
    public HashMap<String, Integer> loadShopData() throws PersistanceException {
        FileHandle file = Gdx.files.local(shopSaveFileName);

        if (file.exists()) {
            Json json = new Json();
            return json.fromJson(HashMap.class, file);
        }

        HashMap<String, Integer> defaultMap = new HashMap<>();
        defaultMap.put("Tung", 10);
        defaultMap.put("Ballerina", 20);
        defaultMap.put("Lirili", 30);

        saveShopData(defaultMap);

        return defaultMap;
    }

    private void saveLevelData(HashMap<Integer, String> levelNames) throws PersistanceException {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(levelNames);

        FileHandle file = Gdx.files.local(levelSaveFileName);
        file.writeString(jsonString, false);
    }

    @Override
    public HashMap<Integer, String> loadLevelData() throws PersistanceException {
        FileHandle file = Gdx.files.local(levelSaveFileName);

        if (file.exists()) {
            Json json = new Json();
            return json.fromJson(HashMap.class, file);
        }

        HashMap<Integer, String>  defaultMap = new HashMap<>();
        defaultMap.put(1, "67Hills");

        saveLevelData(defaultMap);

        return defaultMap;
    }
}
