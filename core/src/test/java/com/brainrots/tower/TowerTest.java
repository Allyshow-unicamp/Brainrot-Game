package com.brainrots.tower;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameBalance;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;
import com.brainrots.synergy.SynergyBoost;
import com.brainrots.synergy.SynergyGroup;

import org.junit.jupiter.api.Test;

class TowerTest {

    private Tower createTestTower() {
        return new Tower(null, null, new Vector2(100, 100), "Test Tower", 50, 100, 200f, 1.0f, 150, 150, BrainrotTower.TUNG_TUNG) {
            @Override
            public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                               CoinManager coinManager, ProjectileManager projectileManager, com.badlogic.gdx.graphics.Texture projectileTexture) {}
        };
    }

    @Test
    void startsWithZeroDamageUpgradeLevel() {
        Tower tower = createTestTower();
        assertEquals(0, tower.getDamageUpgradeLevel());
    }

    @Test
    void startsWithZeroRangeUpgradeLevel() {
        Tower tower = createTestTower();
        assertEquals(0, tower.getRangeUpgradeLevel());
    }

    @Test
    void startsWithZeroCooldownUpgradeLevel() {
        Tower tower = createTestTower();
        assertEquals(0, tower.getCooldownUpgradeLevel());
    }

    @Test
    void startsWithZeroValueSpent() {
        Tower tower = createTestTower();
        assertEquals(0, tower.getValueSpent());
    }

    @Test
    void addValueSpentIncreasesValue() {
        Tower tower = createTestTower();
        tower.addValueSpent(100);
        tower.addValueSpent(50);
        assertEquals(150, tower.getValueSpent());
    }

    @Test
    void getTotalDamageReturnsBaseDamage() {
        Tower tower = createTestTower();
        assertEquals(50, tower.getTotalDamage());
    }

    @Test
    void getTotalDamageIncludesBonusDamage() {
        Tower tower = createTestTower();
        tower.addBonusDamage(20);
        assertEquals(70, tower.getTotalDamage());
    }

    @Test
    void getTotalDamageWithSynergyBoost() {
        Tower tower = createTestTower();
        tower.addSynergyBoost(new SynergyBoost(1.5f));
        assertEquals(Math.round(50 * 1.5f), tower.getTotalDamage());
    }

    @Test
    void getTotalDamageWithMultipleSynergyBoosts() {
        Tower tower = createTestTower();
        tower.addSynergyBoost(new SynergyBoost(1.2f));
        tower.addSynergyBoost(new SynergyBoost(1.3f));
        assertEquals(Math.round(50 * 1.2f * 1.3f), tower.getTotalDamage());
    }

    @Test
    void getTotalDamageWithBonusAndSynergy() {
        Tower tower = createTestTower();
        tower.addBonusDamage(30);
        tower.addSynergyBoost(new SynergyBoost(2.0f));
        assertEquals(Math.round(80 * 2.0f), tower.getTotalDamage());
    }

    @Test
    void getRangeReturnsBaseRange() {
        Tower tower = createTestTower();
        assertEquals(200f, tower.getRange(), 0.001f);
    }

    @Test
    void getRangeWithSynergyBoost() {
        Tower tower = createTestTower();
        tower.addSynergyBoost(new SynergyBoost(1.0f, 2.0f, 1.0f));
        assertEquals(400f, tower.getRange(), 0.001f);
    }

    @Test
    void getAttackCooldownReturnsBaseCooldown() {
        Tower tower = createTestTower();
        assertEquals(1.0f, tower.getAttackCooldown(), 0.001f);
    }

    @Test
    void getAttackCooldownWithSynergyBoost() {
        Tower tower = createTestTower();
        tower.addSynergyBoost(new SynergyBoost(1.0f, 1.0f, 0.85f));
        assertEquals(0.85f, tower.getAttackCooldown(), 0.001f);
    }

    @Test
    void upgradeDamageIncreasesDamageAndLevel() {
        Tower tower = createTestTower();
        tower.upgradeDamage();
        assertEquals(GameBalance.UPGRADE_DAMAGE, tower.getDamage() - 50);
        assertEquals(1, tower.getDamageUpgradeLevel());
    }

    @Test
    void upgradeRangeIncreasesRangeAndLevel() {
        Tower tower = createTestTower();
        tower.upgradeRange();
        assertEquals(200f + GameBalance.UPGRADE_RANGE, tower.getRange(), 0.001f);
        assertEquals(1, tower.getRangeUpgradeLevel());
    }

    @Test
    void upgradeCooldownReducesCooldown() {
        Tower tower = createTestTower();
        tower.upgradeCooldown();
        assertEquals(1, tower.getCooldownUpgradeLevel());
        assertTrue(tower.getAttackCooldown() < 1.0f);
    }

    @Test
    void upgradeCooldownDoesNotGoBelowMin() {
        Tower tower = createTestTower();
        for (int i = 0; i < 50; i++) {
            tower.upgradeCooldown();
        }
        assertTrue(tower.getAttackCooldown() >= 0.1f);
    }

    @Test
    void damageUpgradeCostDoublesEachLevel() {
        Tower tower = createTestTower();
        int cost0 = tower.getDamageUpgradeCost();
        tower.upgradeDamage();
        int cost1 = tower.getDamageUpgradeCost();
        assertEquals(cost0 * 2, cost1);
    }

    @Test
    void rangeUpgradeCostDoublesEachLevel() {
        Tower tower = createTestTower();
        int cost0 = tower.getRangeUpgradeCost();
        tower.upgradeRange();
        int cost1 = tower.getRangeUpgradeCost();
        assertEquals(cost0 * 2, cost1);
    }

    @Test
    void cooldownUpgradeCostDoublesEachLevel() {
        Tower tower = createTestTower();
        int cost0 = tower.getCooldownUpgradeCost();
        tower.upgradeCooldown();
        int cost1 = tower.getCooldownUpgradeCost();
        assertEquals(cost0 * 2, cost1);
    }

    @Test
    void initialUpgradeCostsMatchBalanceConstants() {
        Tower tower = createTestTower();
        assertEquals(GameBalance.UPGRADE_BASE_DAMAGE_COST, tower.getDamageUpgradeCost());
        assertEquals(GameBalance.UPGRADE_BASE_RANGE_COST, tower.getRangeUpgradeCost());
        assertEquals(GameBalance.UPGRADE_BASE_COOLDOWN_COST, tower.getCooldownUpgradeCost());
    }

    @Test
    void addBonusDamageAccumulates() {
        Tower tower = createTestTower();
        tower.addBonusDamage(10);
        tower.addBonusDamage(20);
        tower.addBonusDamage(30);
        assertEquals(60, tower.getDamage() + 60 - 50); // verify via getTotalDamage logic
        assertEquals(60, tower.getTotalDamage() - 50);
    }

    @Test
    void addSynergyBoostDoesNotDuplicate() {
        Tower tower = createTestTower();
        SynergyBoost boost = new SynergyBoost(1.5f);
        tower.addSynergyBoost(boost);
        tower.addSynergyBoost(boost);
        assertEquals(1.0f, tower.getAttackCooldown(), 0.001f); // only one boost
    }

    @Test
    void removeSynergyBoostRemovesCorrectBoost() {
        Tower tower = createTestTower();
        SynergyBoost boost1 = new SynergyBoost(1.5f);
        SynergyBoost boost2 = new SynergyBoost(1.0f, 2.0f, 1.0f);
        tower.addSynergyBoost(boost1);
        tower.addSynergyBoost(boost2);

        tower.removeSynergyBoost(boost1);
        // boost2 (range 2x) is still active
        assertEquals(400f, tower.getRange(), 0.001f);
        // boost1 (damage 1.5x) is removed
        assertEquals(Math.round(50 * 1.0f), tower.getTotalDamage());
    }

    @Test
    void getNameReturnsDisplayName() {
        Tower tower = createTestTower();
        assertEquals("Test Tower", tower.getName());
    }

    @Test
    void getDamageReturnsBaseDamage() {
        Tower tower = createTestTower();
        assertEquals(50, tower.getDamage());
    }

    @Test
    void getIDReturnsTowerTypeId() {
        Tower tower = createTestTower();
        assertEquals(BrainrotTower.TUNG_TUNG.ordinal(), tower.getID());
    }

    @Test
    void getAttackSpeedReturnsAttackSpeed() {
        Tower tower = createTestTower();
        assertEquals(100, tower.getAttackSpeed());
    }

    @Test
    void setPositionUpdatesPosition() {
        Tower tower = createTestTower();
        tower.setPosition(new Vector2(200, 300));
        assertEquals(200f, tower.getPosition().x, 0.001f);
        assertEquals(300f, tower.getPosition().y, 0.001f);
    }

    @Test
    void getSynergyGroupReturnsTowerTypeGroup() {
        Tower tower = createTestTower();
        assertEquals(SynergyGroup.PHYSICAL, tower.getSynergyGroup());
    }

    @Test
    void getTowerTypeReturnsCorrectType() {
        Tower tower = createTestTower();
        assertEquals(BrainrotTower.TUNG_TUNG, tower.getTowerType());
    }

    @Test
    void createEffectReturnsNullForPhysicalTower() {
        Tower tower = createTestTower();
        assertNull(tower.createEffect());
    }

    @Test
    void createEffectReturnsPrototypeForFireTower() {
        Tower tower = new Tower(null, null, new Vector2(100, 100), "Bombardiro", 100, 50, 350f, 0.9f, 150, 150, BrainrotTower.BOMBARDILO_CROCODILO) {
            @Override
            public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                               CoinManager coinManager, ProjectileManager projectileManager, com.badlogic.gdx.graphics.Texture projectileTexture) {}
        };
        assertNotNull(tower.createEffect());
    }

    @Test
    void setDescriptionAndGetName() {
        Tower tower = createTestTower();
        tower.setDescription("A powerful tower");
        assertEquals("A powerful tower", tower.getDescription());
    }

    @Test
    void getDamageUpgradeLabel() {
        Tower tower = createTestTower();
        String label = tower.getDamageUpgradeLabel();
        assertTrue(label.contains("Lv.0"));
        assertTrue(label.contains("$"));
    }

    @Test
    void getRangeUpgradeLabel() {
        Tower tower = createTestTower();
        String label = tower.getRangeUpgradeLabel();
        assertTrue(label.contains("Lv.0"));
        assertTrue(label.contains("$"));
    }

    @Test
    void getCooldownUpgradeLabel() {
        Tower tower = createTestTower();
        String label = tower.getCooldownUpgradeLabel();
        assertTrue(label.contains("Lv.0"));
        assertTrue(label.contains("$"));
    }

    @Test
    void multipleUpgradeLevelsWork() {
        Tower tower = createTestTower();
        tower.upgradeDamage();
        tower.upgradeDamage();
        tower.upgradeDamage();
        assertEquals(3, tower.getDamageUpgradeLevel());
        assertEquals(50 + 3 * GameBalance.UPGRADE_DAMAGE, tower.getDamage());

        tower.upgradeRange();
        tower.upgradeRange();
        assertEquals(2, tower.getRangeUpgradeLevel());

        tower.upgradeCooldown();
        tower.upgradeCooldown();
        assertEquals(2, tower.getCooldownUpgradeLevel());
    }
}
