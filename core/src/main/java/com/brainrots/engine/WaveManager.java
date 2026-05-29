package com.brainrots.engine;

import java.util.Queue;

import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.ConcreteEnemy;
import com.brainrots.enemy.Enemy;

public class WaveManager {
    private int currentWave = 1;
    private int maxWaves;

    private float spawnTimer = 0f;
    private float spawnCooldown = 1.5f;

    private Queue<String> enemiesToSpawn;
    private boolean isWaveActive = false;

    public WaveManager(int maxWaves) {
        this.maxWaves = maxWaves;
    }

    public void startNextWave(Queue<String> enemiesToSpawn) {
        if (this.currentWave <= maxWaves) {
            this.enemiesToSpawn = enemiesToSpawn;
            this.isWaveActive = true;
            this.currentWave++;
        }
    }

    public void update(float delta, LevelManager levelManager, GameResourceManager resourceManager) {
        if (!isWaveActive) return;

        if (!enemiesToSpawn.isEmpty()) {
            spawnTimer += delta;

            if (spawnTimer >= spawnCooldown) {
                String enemyAsset = enemiesToSpawn.poll();

                Enemy newEnemy = new ConcreteEnemy(levelManager.getPath(), resourceManager.getTexture(enemyAsset), 25, currentWave * 500);

                levelManager.spawnEnemy(newEnemy);

                spawnTimer = 0f;
            }
        } else {
            if (levelManager.getActiveEnemies().isEmpty()) {
                isWaveActive = false;
            }
        }
    }
}
