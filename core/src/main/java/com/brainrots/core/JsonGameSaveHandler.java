package com.brainrots.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.brainrots.exception.PersistanceException;
import com.brainrots.interfaces.GameSaveHandler;

/** Persistência JSON. Serializa/deserializa perfil, loja e dados de níveis. */
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
    public void saveProfile(PlayerProfile playerProfile) throws PersistanceException {
        try {
            Json json = new Json();
            json.setOutputType(OutputType.json);
            String jsonString = json.prettyPrint(playerProfile);

            FileHandle file = Gdx.files.local(profileSaveFileName);
            file.writeString(jsonString, false);
        } catch (Exception e) {
            throw new PersistanceException("Error while saving profile");
        }
    }

    @Override
    public PlayerProfile loadProfile() throws PersistanceException {
        try {
            FileHandle file = Gdx.files.local(profileSaveFileName);

            if (file.exists() && file != null) {
                Json json = new Json();
                PlayerProfile profile = json.fromJson(PlayerProfile.class, file);
                return profile;
            }

            HashMap<String, Integer> defaultMap = new HashMap<>();
            for (int i = 1; i <= 5; i++) {
                defaultMap.put(i + "", 0);
            }

            ArrayList<String> defaultUnlocked = new ArrayList<>();
            defaultUnlocked.add("1_tung_tung");

            return new PlayerProfile(0, defaultMap, defaultUnlocked);
        } catch (Exception e) {
            throw new PersistanceException("Error while loading profile");
        }
    }

    @Override
    public HashMap<String, ShopTowerData> loadShopData() throws PersistanceException {
        try {
            FileHandle file = Gdx.files.local(shopSaveFileName);

            if (file.exists()) {

                JsonReader reader = new JsonReader();
                JsonValue root = reader.parse(file);
                if (root.child != null && root.child.has("class")) {
                    throw new PersistanceException("Error while reading Shop Data");
                }
                HashMap<String, ShopTowerData> result = new HashMap<>();
                for (JsonValue entry = root.child; entry != null; entry = entry.next) {
                    String id = entry.name;
                    String name = entry.getString("name", "");
                    String description = entry.getString("description", "");
                    String behaviorDescription = entry.getString("behaviorDescription", "");
                    int price = entry.getInt("price", 0);
                    result.put(id, new ShopTowerData(id, name, description, behaviorDescription, price));
                }
                return result;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PersistanceException("Error while reading shop data");
        }
    }

    private void saveLevelData(HashMap<String, String> levelNames) throws PersistanceException {
        try {
            Json json = new Json();
            json.setOutputType(OutputType.json);
            String jsonString = json.prettyPrint(levelNames);

            FileHandle file = Gdx.files.local(levelSaveFileName);
            file.writeString(jsonString, false);
        } catch (Exception e) {
            throw new PersistanceException("Error while saving level data");
        }
    }

    @Override
    public HashMap<String, String> loadLevelData() throws PersistanceException {
        try {
            FileHandle file = Gdx.files.local(levelSaveFileName);

            if (file.exists()) {
                Json json = new Json();
                return json.fromJson(HashMap.class, file);
            }

            HashMap<String, String> defaultMap = new HashMap<>();
            defaultMap.put(1 + "", Assets.MAP_67_HILLS_ID);
            defaultMap.put(2 + "", Assets.MAP_WINTER_AURA_ID);
            defaultMap.put(3 + "", Assets.MAP_SIGMA_VOLCANO_ID);
            defaultMap.put(4 + "", Assets.MAP_MINING_RIZZ_ID);
            defaultMap.put(5 + "", Assets.MAP_BETA_JUNGLE_ID);

            saveLevelData(defaultMap);

            return defaultMap;
        } catch (Exception e) {
            throw new PersistanceException("Error while loading level data");
        }
    }
}
