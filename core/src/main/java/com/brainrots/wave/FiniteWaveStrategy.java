package com.brainrots.wave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.file.Paths;

/** Estratégia de waves finitas: lê dados do arquivo waves.json do mapa. */
public class FiniteWaveStrategy implements WaveGenerationStrategy {

    private JsonValue allWaves;

    /** @param levelName nome do mapa (ex: "67Hills") — localiza Maps/{levelName}/waves.json */
    public FiniteWaveStrategy(String levelName) {
        String path = Paths.get("Maps", levelName, "waves.json").toString();
        this.allWaves = new JsonReader().parse(Gdx.files.internal(path));
    }

    @Override
    public boolean hasNextWave(int currentWave) {
        return currentWave <= allWaves.size;
    }

    @Override
    public WaveData generateNextWave(int currentWave) {
        JsonValue waveData = allWaves.get(String.valueOf(currentWave));
        WaveData data = new WaveData();

        for (JsonValue enemy : waveData.get("enemies")) {
            String type = enemy.getString("type");
            int count = enemy.getInt("count");
            float interval = enemy.getFloat("spawnInterval");
            int spawnTime = enemy.getInt("spawnTime");
            data.addGroup(type, count, interval, spawnTime);
        }

        return data;
    }
}
