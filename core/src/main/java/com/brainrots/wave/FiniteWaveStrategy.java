package com.brainrots.wave;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.file.Paths;

/**
 * Estratégia de waves finitas: lê os dados do arquivo waves.json do mapa.
 *
 * Mantém compatibilidade total com o formato de waves existente.
 * O JSON é parseado no construtor e as waves são servidas sequencialmente
 * pelo generateNextWave().
 *
 * Quando a última wave é servida, hasNextWave() retorna false.
 */
public class FiniteWaveStrategy implements WaveGenerationStrategy {

    private JsonValue allWaves;

    /**
     * @param levelName nome do mapa (ex: "67Hills") — usado para localizar
     *                  o arquivo Maps/{levelName}/waves.json
     */
    public FiniteWaveStrategy(String levelName) {
        String path = Paths.get("Maps", levelName, "waves.json").toString();
        this.allWaves = new JsonReader().parse(Gdx.files.internal(path));
    }

    @Override
    public boolean hasNextWave(int currentWave) {
        // currentWave é 1-indexed; allWaves é 1-indexed no JSON
        return currentWave < allWaves.size;
    }

    /**
     * Gera WaveData a partir do JSON da wave solicitada.
     * O JSON é estruturado como:
     * {
     *   "1": { "enemies": [ { "type": "...", "count": N, "spawnInterval": F, "spawnTime": N }, ... ] },
     *   "2": { ... }
     * }
     */
    @Override
    public WaveData generateNextWave(int currentWave) {
        // currentWave é o número da próxima wave (já incrementado pelo WaveManager)
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
