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

class TowerCoinTest {

    private TowerCoin createCoinTower(int coinsPerTower) {
        return new TowerCoin(null, null, new Vector2(100, 100), "Coin Tower",
            GameBalance.TOWER_COIN_RANGE, GameBalance.TOWER_COIN_COOLDOWN,
            150, 150, BrainrotTower.CAPPUCCINO_ASSASSINO, coinsPerTower);
    }

    private Tower createNearbyTower(Vector2 pos) {
        return new Tower(null, null, pos, "Nearby", 50, 100, 200f, 1.0f, 150, 150, BrainrotTower.TUNG_TUNG) {
            @Override
            public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                               CoinManager coinManager, ProjectileManager projectileManager, com.badlogic.gdx.graphics.Texture pt) {}
        };
    }

    @Test
    void coinTowerHasZeroDamage() {
        TowerCoin tower = createCoinTower(7);
        assertEquals(0, tower.getDamage());
    }

    @Test
    void createEffectReturnsNull() {
        TowerCoin tower = createCoinTower(7);
        assertNull(tower.createEffect());
    }

    @Test
    void updateGeneratesCoinsForNearbyTowers() {
        TowerCoin tower = createCoinTower(7);
        CoinManager cm = new CoinManager(0);
        Array<Tower> towers = new Array<>();
        towers.add(tower);
        towers.add(createNearbyTower(new Vector2(110, 110)));
        towers.add(createNearbyTower(new Vector2(120, 120)));

        tower.update(10.0f, new Array<>(), towers, cm, new ProjectileManager(), null);
        assertEquals(14, cm.getCoins());
    }

    @Test
    void updateDoesNotGenerateForSelf() {
        TowerCoin tower = createCoinTower(7);
        CoinManager cm = new CoinManager(0);
        Array<Tower> towers = new Array<>();
        towers.add(tower);

        tower.update(10.0f, new Array<>(), towers, cm, new ProjectileManager(), null);
        assertEquals(0, cm.getCoins());
    }

    @Test
    void updateRespectsCooldown() {
        TowerCoin tower = createCoinTower(7);
        CoinManager cm = new CoinManager(0);
        Array<Tower> towers = new Array<>();
        towers.add(tower);
        towers.add(createNearbyTower(new Vector2(110, 110)));

        tower.update(0.0f, new Array<>(), towers, cm, new ProjectileManager(), null);
        int coinsAfterFirst = cm.getCoins();
        tower.update(1.0f, new Array<>(), towers, cm, new ProjectileManager(), null);
        assertEquals(coinsAfterFirst, cm.getCoins());
    }

    @Test
    void upgradeDamageIncreasesCoinsPerTower() {
        TowerCoin tower = createCoinTower(7);
        tower.upgradeDamage();
        assertEquals(1, tower.getDamageUpgradeLevel());
    }

    @Test
    void getDamageUpgradeLabelShowsCoinsPerTower() {
        TowerCoin tower = createCoinTower(7);
        String label = tower.getDamageUpgradeLabel();
        assertTrue(label.contains("Coins/Tower"));
    }

    @Test
    void getSynergyGroupIsSupport() {
        TowerCoin tower = createCoinTower(7);
        assertEquals(SynergyGroup.SUPPORT, tower.getSynergyGroup());
    }

    @Test
    void updateOnlyCountsTowersWithinRange() {
        TowerCoin tower = createCoinTower(7);
        CoinManager cm = new CoinManager(0);
        Array<Tower> towers = new Array<>();
        towers.add(tower);
        towers.add(createNearbyTower(new Vector2(1000, 1000)));

        tower.update(10.0f, new Array<>(), towers, cm, new ProjectileManager(), null);
        assertEquals(0, cm.getCoins());
    }
}
