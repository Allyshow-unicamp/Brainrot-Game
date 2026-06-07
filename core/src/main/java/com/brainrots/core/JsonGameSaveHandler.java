package com.brainrots.core;

import java.util.ArrayList;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

public class JsonGameSaveHandler implements GameSaveHandler {
    private String saveFileName;

    public JsonGameSaveHandler(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    @Override
    public void saveProfile(PlayerProfile playerProfile) {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(playerProfile);

        FileHandle file = Gdx.files.local(saveFileName);
        file.writeString(jsonString, false);
    }

    @Override
    public PlayerProfile loadProfile() {
        FileHandle file = Gdx.files.local(saveFileName);

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
}
