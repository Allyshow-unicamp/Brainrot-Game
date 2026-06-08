package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.enemy.EnemyFactory;
import com.brainrots.enemy.EnemySpawner;

public class WaveManager {

    private int currentWave;
    private Array<Vector2> path;
    private EnemyFactory factory = new EnemyFactory();

    private JsonReader reader;
    private JsonValue allWaves;

    private Array<EnemySpawner> activeSpawners;

    private boolean isWaveActive;

    public WaveManager(Array<Vector2> path) {
        this.currentWave = 0;
        this.isWaveActive = false;
        this.reader = new JsonReader();
        this.allWaves = reader.parse(Gdx.files.internal("data/waves.json"));
        this.activeSpawners = new Array<>();
        this.path = path;
    }

    public void startNextWave() {

        currentWave++;

        JsonValue waveData = allWaves.get(String.valueOf(currentWave));

        for(JsonValue enemy : waveData.get("enemies")){
            activeSpawners.add(new EnemySpawner(enemy.getString("type"), enemy.getInt("count"), enemy.getFloat("spawnInterval"),
        enemy.getInt("spawnTime")));
        }

        isWaveActive = true;
    }

    public void update(float delta, LevelManager levelManager, GameResourceManager resourceManager) {
        if (!isWaveActive && currentWave < 5){
            startNextWave();
            return;
        }

        if(activeSpawners.isEmpty()){
            isWaveActive = false;
            return;
        }

        ArrayIterator<EnemySpawner> iterator = activeSpawners.iterator();

        while (iterator.hasNext()) {
            EnemySpawner spawner = iterator.next();

            if(spawner.shouldSpawn(delta)){
                Enemy enemy = factory.spawnEnemy(spawner.getType(), path);
                levelManager.spawnEnemy(enemy);
            }

            if(spawner.isEmpty()){
                activeSpawners.removeValue(spawner, false);
            }
        }

    }
}