package com.brainrots.enemy;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.effect.FireEffect;

import org.junit.jupiter.api.Test;

class EnemyTest {

    private Enemy createTestEnemy(int hp, int speed) {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(0, 0));
        path.add(new Vector2(500, 0));
        path.add(new Vector2(500, 500));
        return new Enemy(path, null, hp, speed, 100, 100, 10);
    }

    private Enemy createTestEnemyWithPath(Array<Vector2> path, int hp, int speed) {
        return new Enemy(path, null, hp, speed, 100, 100, 10);
    }

    @Test
    void enemyStartsWithFullHealth() {
        Enemy enemy = createTestEnemy(100, 100);
        assertEquals(100, enemy.getHp());
    }

    @Test
    void takeDamageReducesHealth() {
        Enemy enemy = createTestEnemy(100, 100);
        enemy.takeDamage(30);
        assertEquals(70, enemy.getHp());
    }

    @Test
    void takeDamageReturnsTrueWhenDead() {
        Enemy enemy = createTestEnemy(100, 100);
        assertTrue(enemy.takeDamage(100));
    }

    @Test
    void takeDamageReturnsFalseWhenAlive() {
        Enemy enemy = createTestEnemy(100, 100);
        assertFalse(enemy.takeDamage(50));
    }

    @Test
    void isDeadWhenHpZero() {
        Enemy enemy = createTestEnemy(100, 100);
        enemy.takeDamage(100);
        assertTrue(enemy.isDead());
    }

    @Test
    void isAliveWhenHpPositive() {
        Enemy enemy = createTestEnemy(100, 100);
        enemy.takeDamage(99);
        assertTrue(enemy.isAlive());
    }

    @Test
    void isNotAliveWhenHpZero() {
        Enemy enemy = createTestEnemy(100, 100);
        enemy.takeDamage(100);
        assertFalse(enemy.isAlive());
    }

    @Test
    void giveCoinOnDeathReturnsReward() {
        Enemy enemy = createTestEnemy(100, 100);
        assertEquals(10, enemy.giveCoinOnDeath());
    }

    @Test
    void getSpeedReturnsCurrentSpeed() {
        Enemy enemy = createTestEnemy(100, 400);
        assertEquals(400, enemy.getSpeed());
    }

    @Test
    void getOriginalSpeedReturnsInitialSpeed() {
        Enemy enemy = createTestEnemy(100, 400);
        assertEquals(400, enemy.getOriginalSpeed());
    }

    @Test
    void positionStartsAtFirstWaypoint() {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(10, 20));
        path.add(new Vector2(100, 200));
        Enemy enemy = createTestEnemyWithPath(path, 100, 100);
        assertEquals(10f, enemy.getPosition().x, 0.001f);
        assertEquals(20f, enemy.getPosition().y, 0.001f);
    }

    @Test
    void enemyMovesTowardsWaypoint() {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(0, 0));
        path.add(new Vector2(1000, 0));
        Enemy enemy = createTestEnemyWithPath(path, 100, 100);

        enemy.update(1.0f);
        assertTrue(enemy.getPosition().x > 0);
        assertEquals(0f, enemy.getPosition().y, 0.001f);
    }

    @Test
    void stunPreventsMovement() {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(0, 0));
        path.add(new Vector2(1000, 0));
        Enemy enemy = createTestEnemyWithPath(path, 100, 100);

        enemy.setStunned(true);
        enemy.update(1.0f);
        assertEquals(0f, enemy.getPosition().x, 0.001f);
    }

    @Test
    void isStunnedReturnsCorrectState() {
        Enemy enemy = createTestEnemy(100, 100);
        assertFalse(enemy.isStunned());
        enemy.setStunned(true);
        assertTrue(enemy.isStunned());
    }

    @Test
    void applySlowReducesSpeed() {
        Enemy enemy = createTestEnemy(100, 100);
        enemy.applySlow(0.5f);
        assertEquals(50, enemy.getSpeed());
    }

    @Test
    void restoreSpeedRestoresOriginalSpeed() {
        Enemy enemy = createTestEnemy(100, 100);
        enemy.applySlow(0.5f);
        enemy.restoreSpeed();
        assertEquals(100, enemy.getSpeed());
    }

    @Test
    void slowStackingDoesNotReapplyReduction() {
        Enemy enemy = createTestEnemy(100, 200);
        enemy.applySlow(0.5f);
        assertEquals(100, enemy.getSpeed());
        enemy.applySlow(0.5f);
        assertEquals(100, enemy.getSpeed());
    }

    @Test
    void restoreSpeedOnlyRestoresWhenStackEmpty() {
        Enemy enemy = createTestEnemy(100, 200);
        enemy.applySlow(0.5f);
        enemy.applySlow(0.5f);
        enemy.restoreSpeed();
        assertEquals(100, enemy.getSpeed());
        enemy.restoreSpeed();
        assertEquals(200, enemy.getSpeed());
    }

    @Test
    void restoreSpeedDoesNotGoBelowZeroStack() {
        Enemy enemy = createTestEnemy(100, 100);
        enemy.restoreSpeed();
        assertEquals(100, enemy.getSpeed());
    }

    @Test
    void applySlowMinimumSpeedIsOne() {
        Enemy enemy = createTestEnemy(100, 1);
        enemy.applySlow(0.001f);
        assertTrue(enemy.getSpeed() >= 1);
    }

    @Test
    void addEffectAddsToActiveEffects() {
        Enemy enemy = createTestEnemy(100, 100);
        FireEffect effect = new FireEffect(3f, 7);
        enemy.addEffect(effect);
        assertEquals(1, enemy.getActiveEffects().size);
    }

    @Test
    void enemyProcessesEffectsDuringUpdate() {
        Enemy enemy = createTestEnemy(100, 100);
        FireEffect effect = new FireEffect(3f, 7);
        enemy.addEffect(effect);

        enemy.update(1.0f);
        assertEquals(93, enemy.getHp());
    }

    @Test
    void expiredEffectsAreRemovedDuringUpdate() {
        Enemy enemy = createTestEnemy(100, 100);
        FireEffect effect = new FireEffect(0.5f, 7);
        enemy.addEffect(effect);

        enemy.update(1.0f);
        assertTrue(enemy.getActiveEffects().isEmpty());
    }

    @Test
    void enemyReachesSecondWaypoint() {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(0, 0));
        path.add(new Vector2(50, 0));
        path.add(new Vector2(50, 500));
        Enemy enemy = createTestEnemyWithPath(path, 100, 500);

        enemy.update(0.2f);
        assertEquals(50f, enemy.getPosition().x, 0.001f);
        assertEquals(0f, enemy.getPosition().y, 0.001f);
    }

    @Test
    void enemyReachesEndOfPath() {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(0, 0));
        path.add(new Vector2(10, 0));
        Enemy enemy = createTestEnemyWithPath(path, 100, 1000);

        enemy.update(1.0f);
        assertEquals(10f, enemy.getPosition().x, 0.001f);
        assertEquals(0f, enemy.getPosition().y, 0.001f);
    }

    @Test
    void enemyTakesDamageFromEffectsEvenAfterHpLow() {
        Enemy enemy = createTestEnemy(15, 100);
        FireEffect effect = new FireEffect(3f, 7);
        enemy.addEffect(effect);

        enemy.update(1.0f);
        assertEquals(8, enemy.getHp());
    }

    @Test
    void healthCanGoNegative() {
        Enemy enemy = createTestEnemy(10, 100);
        enemy.takeDamage(100);
        assertTrue(enemy.getHp() < 0);
    }

    @Test
    void nullPathEnemyStartsAtOrigin() {
        Enemy enemy = new Enemy(null, null, 100, 100, 100, 100, 10);
        assertEquals(0f, enemy.getPosition().x, 0.001f);
        assertEquals(0f, enemy.getPosition().y, 0.001f);
    }

    @Test
    void emptyPathEnemyStartsAtOrigin() {
        Enemy enemy = new Enemy(new Array<>(), null, 100, 100, 100, 100, 10);
        assertEquals(0f, enemy.getPosition().x, 0.001f);
        assertEquals(0f, enemy.getPosition().y, 0.001f);
    }
}
