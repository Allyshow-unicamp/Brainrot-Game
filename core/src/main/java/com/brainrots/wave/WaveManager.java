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

/**
 * Gerencia a progressão de waves durante o jogo.
 *
 * Delega a geração dos dados de cada wave para uma
 * WaveGenerationStrategy (Strategy Pattern).
 */
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

    /**
     * Construtor padrão: usa FiniteWaveStrategy (waves do JSON).
     */
    public WaveManager(String levelName, Array<Vector2> path) {
        this(levelName, path, new FiniteWaveStrategy(levelName));
    }

    /**
     * Construtor que aceita uma estratégia personalizada.
     *
     * @param levelName nome do mapa (para fallback de maxWaves)
     * @param path      caminho de waypoints do mapa
     * @param strategy  estratégia de geração de waves
     */
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
        } catch (Exception e) {
            this.maxWaves = Integer.MAX_VALUE;
        }
    }

    /**
     * Construtor usado apenas para testes (evita dependência de Gdx.files).
     */
    public WaveManager(Array<Vector2> path, WaveGenerationStrategy strategy, int maxWaves) {
        this.currentWave = 0;
        this.isWaveActive = false;
        this.activeSpawners = new Array<>();
        this.path = path;
        this.pausedWave = true;
        this.strategy = strategy;
        this.maxWaves = maxWaves;
    }

    /**
     * Troca a estratégia de geração de waves em tempo de execução.
     */
    public void setStrategy(WaveGenerationStrategy strategy) {
        this.strategy = strategy;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getMaxWaves() {
        return maxWaves;
    }

    /**
     * Inicia a próxima wave usando a estratégia atual.
     * A estratégia gera os WaveData, que são convertidos em EnemySpawners.
     */
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

    public void update(float delta, Array<Enemy> activeEnemies) {
        // Se não há wave ativa, a estratégia tem próxima wave e o jogo não está pausado,
        // inicia automaticamente a próxima wave (comportamento original).
        if (!isWaveActive && strategy.hasNextWave(currentWave) && !pausedWave) {
            startNextWave();
            return;
        }

        // Quando todos os spawners acabaram e não há inimigos vivos,
        // marca a wave como concluída (isWaveActive = false).
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
