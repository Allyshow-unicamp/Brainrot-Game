package com.brainrots.wave;

import java.nio.file.Paths;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.brainrots.enemy.Enemy;
import com.brainrots.enemy.EnemyFactory;
import com.brainrots.enemy.EnemySpawner;

/** Gerencia progressão de waves. Delega geração de dados para WaveGenerationStrategy. */
public class WaveManager {

    private int currentWave;
    private int maxWaves;
    private Array<Vector2> path;
    private EnemyFactory factory = EnemyFactory.getEnemyFactory();
    private Array<EnemySpawner> activeSpawners;
    private boolean isWaveActive;
    private boolean pausedWave;
    private WaveGenerationStrategy strategy;
    private static final String LEVELS_DIR = "Maps";

    public WaveManager(String levelName, Array<Vector2> path) {
        this(levelName, path, new FiniteWaveStrategy(levelName));
    }

    public WaveManager(String levelName, Array<Vector2> path, WaveGenerationStrategy strategy) {
        this.currentWave = 0;
        this.isWaveActive = false;
        this.activeSpawners = new Array<>();
        this.path = path;
        this.pausedWave = true;
        this.strategy = strategy;

        // Calcula maxWaves a partir do JSON para compatibilidade com o HUD
        String jsonPath = Paths.get(LEVELS_DIR, levelName, "waves.json").toString();
        try {
            JsonValue allWaves = new JsonReader().parse(Gdx.files.internal(jsonPath));
            this.maxWaves = allWaves.size;
        } catch (RuntimeException e) {
            this.maxWaves = Integer.MAX_VALUE;
        }
    }

    /* Para testes: evita dependência de Gdx.files */
    public WaveManager(Array<Vector2> path, WaveGenerationStrategy strategy, int maxWaves) {
        this.currentWave = 0;
        this.isWaveActive = false;
        this.activeSpawners = new Array<>();
        this.path = path;
        this.pausedWave = true;
        this.strategy = strategy;
        this.maxWaves = maxWaves;
    }

    public void setStrategy(WaveGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getMaxWaves() {
        return maxWaves;
    }

    public void startNextWave() {
        if (!strategy.hasNextWave(currentWave)) {
            return; // Não há mais waves (modo finito, já completou)
        }

        currentWave++;
        WaveData waveData = strategy.generateNextWave(currentWave);

        for (WaveData.SpawnGroup group : waveData.getGroups()) {
            activeSpawners.add(new EnemySpawner(
                group.type, group.count, group.spawnInterval, group.spawnTime
            ));
        }

        isWaveActive = true;
    }

    public void pauseWaveGeneration(){
        pausedWave = true;
    }

    public void resumeWaveGeneration(){
        pausedWave = false;
    }

    public boolean isPausedWave() {
        return pausedWave;
    }

    public boolean isWaveActive() {
        return isWaveActive;
    }

    /* Inicia próxima wave automaticamente se não há wave ativa e o jogo não está pausado */
    public void update(float delta, Array<Enemy> activeEnemies) {
        if (!isWaveActive && strategy.hasNextWave(currentWave) && !pausedWave) {
            startNextWave();
            return;
        }

        /* Wave concluída quando todos os spawners terminaram e não há inimigos vivos */
        if (activeSpawners.isEmpty() && activeEnemies.isEmpty()) {
            if (isWaveActive) {
                pausedWave = true;
            }
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
