package com.brainrots.engine.mode;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.utils.Array;
import com.brainrots.engine.Base;
import com.brainrots.mode.GameOutcome;
import com.brainrots.mode.StandardMode;
import com.brainrots.wave.WaveManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Testes para StandardMode.
 * Usam WaveManager e Base reais para testar a lógica de avaliação,
 * sem depender de mocking de libGDX.
 *
 * WaveManager é instanciado com um nome de mapa inválido que
 * fará o JsonReader lançar exceção, mas o construtor original
 * lida com isso. Criamos um WaveManager substituindo a estratégia
 * para evitar dependência de arquivos.
 */
class GameModeTest {

    private StandardMode standardMode;

    @BeforeEach
    void setUp() {
        standardMode = new StandardMode();
    }

    @Test
    void standardModeDefeatsWhenBaseIsDead() {
        Base deadBase = new Base(100, null);
        deadBase.takeDamage(100);
        assertEquals(GameOutcome.DEFEAT, standardMode.evaluate(null, deadBase, null, new Array<>()));
    }

    @Test
    void standardModeVictoryWhenAllWavesCleared() {
        Base aliveBase = new Base(100, null);
        // Avançamos todas as waves manualmente para que currentWave >= maxWaves
        WaveManager wm = createWaveManager(5, 5);
        for (int i = 0; i < 5; i++) {
            wm.startNextWave();
        }
        assertEquals(GameOutcome.VICTORY,
            standardMode.evaluate(null, aliveBase, wm, new Array<>()));
    }

    @Test
    void standardModeNoOutcomeWhenWavesOngoing() {
        Base aliveBase = new Base(100, null);
        Array<EnemyStub> enemies = new Array<>();
        enemies.add(new EnemyStub());

        assertEquals(GameOutcome.NONE,
            standardMode.evaluate(null, aliveBase, createWaveManager(3, 5), convertEnemies(enemies)));
    }

    @Test
    void standardModeNoVictoryWithEnemiesAlive() {
        Base aliveBase = new Base(100, null);
        Array<EnemyStub> enemies = new Array<>();
        enemies.add(new EnemyStub());

        assertEquals(GameOutcome.NONE,
            standardMode.evaluate(null, aliveBase, createWaveManager(5, 5), convertEnemies(enemies)));
    }

    @Test
    void standardModeNoVictoryBeforeLastWave() {
        Base aliveBase = new Base(100, null);
        assertEquals(GameOutcome.NONE,
            standardMode.evaluate(null, aliveBase, createWaveManager(4, 5), new Array<>()));
    }

    @Test
    void standardModeHudSuffix() {
        assertEquals("", standardMode.getHudSuffix());
    }

    /**
     * Cria um WaveManager com estratégia controlada para teste.
     * Usa o construtor de teste que não depende de Gdx.files.
     */
    private WaveManager createWaveManager(int currentWave, int maxWaves) {
        Array<com.badlogic.gdx.math.Vector2> emptyPath = new Array<>();
        WaveManager wm = new WaveManager(emptyPath,
            new com.brainrots.wave.WaveGenerationStrategy() {
                @Override
                public boolean hasNextWave(int cw) { return cw < maxWaves; }
                @Override
                public com.brainrots.wave.WaveData generateNextWave(int cw) {
                    return new com.brainrots.wave.WaveData();
                }
            },
            maxWaves
        );
        return wm;
    }

    /**
     * Test double para Enemy — usa path com 1 elemento para evitar
     * IndexOutOfBoundsException no construtor.
     */
    private static class EnemyStub extends com.brainrots.enemy.Enemy {
        EnemyStub() {
            super(createSinglePointPath(), null, 100, 100, 50, 50, 100);
        }
        private static Array<com.badlogic.gdx.math.Vector2> createSinglePointPath() {
            Array<com.badlogic.gdx.math.Vector2> p = new Array<>();
            p.add(new com.badlogic.gdx.math.Vector2(0, 0));
            return p;
        }
    }

    private Array<com.brainrots.enemy.Enemy> convertEnemies(Array<EnemyStub> stubs) {
        Array<com.brainrots.enemy.Enemy> enemies = new Array<>();
        for (EnemyStub s : stubs) enemies.add(s);
        return enemies;
    }
}
