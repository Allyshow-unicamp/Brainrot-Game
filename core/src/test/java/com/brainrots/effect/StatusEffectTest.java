package com.brainrots.effect;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;

import org.junit.jupiter.api.Test;

class StatusEffectTest {

    private Enemy createTestEnemy() {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(0, 0));
        path.add(new Vector2(100, 0));
        return new Enemy(path, null, 100, 100, 100, 100, 10);
    }

    @Test
    void fireEffectDealsDamagePerTick() {
        FireEffect effect = new FireEffect(3f, 7);
        Enemy enemy = createTestEnemy();
        int hpBefore = enemy.getHp();
        effect.onApply(enemy);
        effect.tick(enemy, 1.0f);
        assertEquals(hpBefore - 7, enemy.getHp());
    }

    @Test
    void fireEffectCopyCreatesIndependentInstance() {
        FireEffect original = new FireEffect(3f, 7);
        FireEffect copy = (FireEffect) original.copy();
        assertNotSame(original, copy);
        assertEquals(original.getDamagePerTick(), copy.getDamagePerTick());
        assertEquals(original.getDuration(), copy.getDuration());
    }

    @Test
    void fireEffectExpiresAfterDuration() {
        FireEffect effect = new FireEffect(2f, 7);
        Enemy enemy = createTestEnemy();

        effect.update(enemy, 1.0f);
        assertFalse(effect.isExpired());

        effect.update(enemy, 1.5f);
        assertTrue(effect.isExpired());
    }

    @Test
    void fireEffectRemainingTimeDecrements() {
        FireEffect effect = new FireEffect(5f, 7);
        assertEquals(5f, effect.getRemainingTime(), 0.001f);

        effect.update(createTestEnemy(), 2.0f);
        assertEquals(3f, effect.getRemainingTime(), 0.001f);
    }

    @Test
    void statusEffectUpdateCallsTickOnApplyAndOnExpire() {
        int[] counter = {0, 0, 0}; // apply, tick, expire
        StatusEffect effect = new StatusEffect(2f) {
            @Override
            public void onApply(Enemy enemy) { counter[0]++; }
            @Override
            public void tick(Enemy enemy, float delta) { counter[1]++; }
            @Override
            public void onExpire(Enemy enemy) { counter[2]++; }
            @Override
            public StatusEffect copy() { return null; }
        };
        Enemy enemy = createTestEnemy();

        effect.update(enemy, 1.0f);
        assertEquals(1, counter[1]);
        assertEquals(0, counter[2]);

        effect.update(enemy, 1.5f);
        assertEquals(2, counter[1]);
        assertEquals(1, counter[2]);
    }

    @Test
    void statusEffectDoesNotTickAfterExpiry() {
        int[] tickCount = {0};
        StatusEffect effect = new StatusEffect(1f) {
            @Override
            public void onApply(Enemy enemy) {}
            @Override
            public void tick(Enemy enemy, float delta) { tickCount[0]++; }
            @Override
            public void onExpire(Enemy enemy) {}
            @Override
            public StatusEffect copy() { return null; }
        };
        Enemy enemy = createTestEnemy();

        effect.update(enemy, 2.0f);
        assertEquals(1, tickCount[0]);

        effect.update(enemy, 1.0f);
        assertEquals(1, tickCount[0]);
    }

    @Test
    void statusEffectNotExpiredAtStart() {
        StatusEffect effect = new StatusEffect(5f) {
            @Override public void onApply(Enemy e) {}
            @Override public void tick(Enemy e, float d) {}
            @Override public void onExpire(Enemy e) {}
            @Override public StatusEffect copy() { return null; }
        };
        assertFalse(effect.isExpired());
        assertEquals(5f, effect.getDuration());
    }

    @Test
    void statusEffectSetRemainingTime() {
        StatusEffect effect = new StatusEffect(5f) {
            @Override public void onApply(Enemy e) {}
            @Override public void tick(Enemy e, float d) {}
            @Override public void onExpire(Enemy e) {}
            @Override public StatusEffect copy() { return null; }
        };
        effect.setRemainingTime(2.0f);
        assertEquals(2.0f, effect.getRemainingTime());
    }

    @Test
    void enemyWithMultipleEffectsProcessesAll() {
        Array<Vector2> path = new Array<>();
        path.add(new Vector2(0, 0));
        path.add(new Vector2(100, 0));
        Enemy enemy = new Enemy(path, null, 200, 100, 100, 100, 10);

        FireEffect fire1 = new FireEffect(2f, 5);
        FireEffect fire2 = new FireEffect(3f, 3);
        enemy.addEffect(fire1);
        enemy.addEffect(fire2);

        assertEquals(2, enemy.getActiveEffects().size);
        enemy.update(1.0f);
        assertEquals(200 - 5 - 3, enemy.getHp());
    }

    @Test
    void fireEffectMultipleTicksAccumulate() {
        FireEffect effect = new FireEffect(5f, 10);
        Enemy enemy = createTestEnemy();

        effect.tick(enemy, 1f);
        effect.tick(enemy, 1f);
        effect.tick(enemy, 1f);

        assertEquals(100 - 30, enemy.getHp());
    }

    @Test
    void fireEffectOnApplyDoesNotDealDamage() {
        FireEffect effect = new FireEffect(3f, 7);
        Enemy enemy = createTestEnemy();
        effect.onApply(enemy);
        assertEquals(100, enemy.getHp());
    }

    @Test
    void fireEffectOnExpireDoesNotDealDamage() {
        FireEffect effect = new FireEffect(0.5f, 7);
        Enemy enemy = createTestEnemy();
        effect.update(enemy, 0.6f);
        int hpAfterExpiry = enemy.getHp();
        effect.update(enemy, 1.0f);
        assertEquals(hpAfterExpiry, enemy.getHp());
    }
}
