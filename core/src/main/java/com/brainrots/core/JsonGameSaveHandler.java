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
            PlayerProfile profile = json.fromJson(PlayerProfile.class, file);
            return profile;
        }

        HashMap<String, Integer> defaultMap = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            defaultMap.put(i + "", 0);
        }

        ArrayList<String> defaultUnlocked = new ArrayList<>();
        defaultUnlocked.add("tung_tung");

        return new PlayerProfile(0, defaultMap, defaultUnlocked);
    }

    private void saveShopData(HashMap<String, ShopTowerData> shopData) throws PersistanceException {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(shopData);

        FileHandle file = Gdx.files.local(shopSaveFileName);
        file.writeString(jsonString, false);
    }

    @Override
    public HashMap<String, ShopTowerData> loadShopData() throws PersistanceException {
        FileHandle file = Gdx.files.local(shopSaveFileName);

        if (file.exists()) {
            try {
                JsonReader reader = new JsonReader();
                JsonValue root = reader.parse(file);
                if (root.child != null && root.child.has("class")) {
                    return createDefaultShopData();
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
            } catch (Exception e) {
                e.printStackTrace();
                return createDefaultShopData();
            }
        }

        HashMap<String, ShopTowerData> defaultMap = createDefaultShopData();
        saveShopData(defaultMap);
        return defaultMap;
    }

    private HashMap<String, ShopTowerData> createDefaultShopData() {
        HashMap<String, ShopTowerData> defaultMap = new HashMap<>();
        defaultMap.put("tung_tung", new ShopTowerData("tung_tung", "Tung Tung Tung Sahur",
                "Basic tower that fires projectiles at a single enemy", "Projectile damage", 0));
        defaultMap.put("bombardiro_crocodilo", new ShopTowerData("bombardiro_crocodilo", "Bombardiro Crocodilo",
                "Fires an explosive projectile that damages multiple enemies in an area", "Area projectile damage", 200));
        defaultMap.put("nuclearo_dinosauro", new ShopTowerData("nuclearo_dinosauro", "Nuclearo Dinosauro",
                "Deals instant area damage to all enemies within range", "Instant area damage", 150));
        defaultMap.put("cappuccino_assassino", new ShopTowerData("cappuccino_assassino", "Cappuccino Assassino",
                "Generates coins each turn based on nearby allied towers", "Coin generator", 100));
        defaultMap.put("ballerina_cappuccina", new ShopTowerData("ballerina_cappuccina", "Ballerina Cappuccina",
                "Boosts the damage of nearby allied towers", "Tower support", 120));
        return defaultMap;
    }

    private void saveLevelData(HashMap<String, String> levelNames) throws PersistanceException {
        Json json = new Json();
        json.setOutputType(OutputType.json);
        String jsonString = json.prettyPrint(levelNames);

        FileHandle file = Gdx.files.local(levelSaveFileName);
        file.writeString(jsonString, false);
    }

    @Override
    public HashMap<String, String> loadLevelData() throws PersistanceException {
        FileHandle file = Gdx.files.local(levelSaveFileName);

        if (file.exists()) {
            Json json = new Json();
            return json.fromJson(HashMap.class, file);
        }

        HashMap<String, String>  defaultMap = new HashMap<>();
        defaultMap.put(1 + "", "67Hills");

        saveLevelData(defaultMap);

        return defaultMap;
    }
}
