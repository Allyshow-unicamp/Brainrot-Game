package com.brainrots.engine;

import java.nio.file.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.brainrots.enemy.Enemy;
import com.brainrots.enemy.EnemyFactory;
import com.brainrots.enemy.EnemySpawner;

public class WaveManager {

    private int currentWave;
    private Array<Vector2> path;
    private EnemyFactory factory = EnemyFactory.getEnemyFactory();
    private int maxWaves;
    private Array<EnemySpawner> activeSpawners;
    private boolean isWaveActive;
    private boolean pausedWave;
    private JsonReader reader;
    private JsonValue allWaves;

    public WaveManager(String levelName, Array<Vector2> path) {
        this.currentWave = 0;
        this.isWaveActive = false;
        this.reader = new JsonReader();
        this.allWaves = reader.parse(Gdx.files.internal(Paths.get("Maps", levelName, "waves.json").toString()));
        this.maxWaves = allWaves.size;
        this.activeSpawners = new Array<>();
        this.path = path;
        this.pausedWave = true;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getMaxWaves() {
        return maxWaves;
    }

    /**
     * Inicia a próxima wave de inimigos.
     */
    public void startNextWave() {
        currentWave++;

        JsonValue waveData = allWaves.get(String.valueOf(currentWave));

        for (JsonValue enemy : waveData.get("enemies")) {
            String type = enemy.getString("type");
            int count = enemy.getInt("count");
            float interval = enemy.getFloat("spawnInterval");
            int spawnTime = enemy.getInt("spawnTime");

            activeSpawners.add(new EnemySpawner(type, count, interval, spawnTime));
        }

        isWaveActive = true;
    }

    public void pauseWaveGeneration(){
        pausedWave = true;
    }

    public void resumeWaveGeneration(){
        pausedWave = false;
    }

    public void update(float delta, Array<Enemy> activeEnemies) {
        if (!isWaveActive && currentWave < maxWaves && pausedWave == false){;
            startNextWave();
            return;
        }

        if (activeSpawners.isEmpty() && activeEnemies.isEmpty() && pausedWave == false) {
            isWaveActive = false;
            return;
        }

        ArrayIterator<EnemySpawner> iterator = activeSpawners.iterator();

        while (iterator.hasNext()) {
            EnemySpawner spawner = iterator.next();

            if (spawner.shouldSpawn(delta)) {
                Enemy enemy = factory.spawnEnemy(spawner.getType(), path);
                activeEnemies.add(enemy);
            }

            if (spawner.isEmpty()) {
                activeSpawners.removeValue(spawner, false);
            }
        }

    }
}
