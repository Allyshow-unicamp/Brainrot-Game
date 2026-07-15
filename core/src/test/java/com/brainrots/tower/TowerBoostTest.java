package com.brainrots.tower;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameBalance;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;
import com.brainrots.synergy.SynergyGroup;

import org.junit.jupiter.api.Test;

class TowerBoostTest {

    private TowerBoost createBoostTower(int boostAmount) {
        return new TowerBoost(null, null, new Vector2(100, 100), "Boost Tower",
            GameBalance.TOWER_BOOST_RANGE, GameBalance.TOWER_BOOST_COOLDOWN,
            150, 150, BrainrotTower.BALLERINA_CAPPUCCINA, boostAmount);
    }

    private Tower createTargetTower(Vector2 pos) {
        return new Tower(null, null, pos, "Target", 50, 100, 200f, 1.0f, 150, 150, BrainrotTower.TUNG_TUNG) {
            @Override
            public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                               CoinManager coinManager, ProjectileManager projectileManager, com.badlogic.gdx.graphics.Texture pt) {}
        };
    }

    @Test
    void boostTowerHasZeroDamage() {
        TowerBoost tower = createBoostTower(7);
        assertEquals(0, tower.getDamage());
    }

    @Test
    void createEffectReturnsNull() {
        TowerBoost tower = createBoostTower(7);
        assertNull(tower.createEffect());
    }

    @Test
    void updateBoostsNearbyTowers() {
        TowerBoost boostTower = createBoostTower(7);
        Tower target = createTargetTower(new Vector2(110, 110));

        Array<Tower> towers = new Array<>();
        towers.add(boostTower);
        towers.add(target);

        boostTower.update(10.0f, new Array<>(), towers, new CoinManager(0), new ProjectileManager(), null);
        assertEquals(57, target.getTotalDamage());
    }

    @Test
    void updateDoesNotBoostSelf() {
        TowerBoost boostTower = createBoostTower(7);
        Array<Tower> towers = new Array<>();
        towers.add(boostTower);

        boostTower.update(10.0f, new Array<>(), towers, new CoinManager(0), new ProjectileManager(), null);
        assertEquals(0, boostTower.getTotalDamage());
    }

    @Test
    void updateRespectsCooldown() {
        TowerBoost boostTower = createBoostTower(7);
        Tower target = createTargetTower(new Vector2(110, 110));
        Array<Tower> towers = new Array<>();
        towers.add(boostTower);
        towers.add(target);

        boostTower.update(0.0f, new Array<>(), towers, new CoinManager(0), new ProjectileManager(), null);
        int damageAfterFirst = target.getTotalDamage();
        boostTower.update(1.0f, new Array<>(), towers, new CoinManager(0), new ProjectileManager(), null);
        assertEquals(damageAfterFirst, target.getTotalDamage());
    }

    @Test
    void upgradeDamageIncreasesBoostAmount() {
        TowerBoost tower = createBoostTower(7);
        tower.upgradeDamage();
        assertEquals(1, tower.getDamageUpgradeLevel());
    }

    @Test
    void getDamageUpgradeLabelShowsBoost() {
        TowerBoost tower = createBoostTower(7);
        String label = tower.getDamageUpgradeLabel();
        assertTrue(label.contains("Boost"));
    }

    @Test
    void getSynergyGroupIsSupport() {
        TowerBoost tower = createBoostTower(7);
        assertEquals(SynergyGroup.SUPPORT, tower.getSynergyGroup());
    }

    @Test
    void updateOnlyBoostsTowersWithinRange() {
        TowerBoost boostTower = createBoostTower(7);
        Tower farTower = createTargetTower(new Vector2(1000, 1000));
        Array<Tower> towers = new Array<>();
        towers.add(boostTower);
        towers.add(farTower);

        boostTower.update(10.0f, new Array<>(), towers, new CoinManager(0), new ProjectileManager(), null);
        assertEquals(50, farTower.getTotalDamage());
    }

    @Test
    void boostAccumulatesOverCooldownCycles() {
        TowerBoost boostTower = createBoostTower(7);
        Tower target = createTargetTower(new Vector2(110, 110));
        Array<Tower> towers = new Array<>();
        towers.add(boostTower);
        towers.add(target);

        boostTower.update(10.0f, new Array<>(), towers, new CoinManager(0), new ProjectileManager(), null);
        boostTower.update(13.0f, new Array<>(), towers, new CoinManager(0), new ProjectileManager(), null);
        assertEquals(50 + 7 + 7, target.getTotalDamage());
    }
}
