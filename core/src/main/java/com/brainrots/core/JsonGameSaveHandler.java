package com.brainrots.core;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.brainrots.exception.PersistanceException;

public class JsonGameSaveHandler implements GameSaveHandler {
    private String profileSaveFileName;
    private String shopSaveFileName;

    public JsonGameSaveHandler(String profileSaveFileName, String shopSaveFileName) {
        this.profileSaveFileName = profileSaveFileName;
        this.shopSaveFileName = shopSaveFileName;
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

        if (file.exists()) {
            Json json = new Json();
            return json.fromJson(PlayerProfile.class, file);
        }

        Map<Integer, Integer> defaultMap = Map.ofEntries(
            Map.entry(1, 0),
            Map.entry(2, 0),
            Map.entry(3, 0),
            Map.entry(4, 0),
            Map.entry(5, 0)
        );

        return new PlayerProfile(0, defaultMap, new ArrayList<>());
    }

    @Override
    public void saveShopData(Map<String, Integer> itemPrices) throws PersistanceException {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(itemPrices);

        FileHandle file = Gdx.files.local(shopSaveFileName);
        file.writeString(jsonString, false);
    }

    @Override
    public Map<String, Integer> loadShopData() throws PersistanceException {
        FileHandle file = Gdx.files.local(shopSaveFileName);

        if (file.exists()) {
            Json json = new Json();
            return json.fromJson(Map.class, Integer.class, file);
        }

        Map<String, Integer> defaultMap = Map.ofEntries(
            Map.entry("Tung", 10),
            Map.entry("Ballerina", 20),
            Map.entry("Lirili", 30)
        );

        saveShopData(defaultMap);

        return defaultMap;
    }
}
