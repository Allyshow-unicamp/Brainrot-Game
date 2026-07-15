package com.brainrots.synergy;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;
import com.brainrots.tower.Tower;
import com.brainrots.tower.BrainrotTower;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SynergyManagerEvaluateTest {

    private SynergyManager manager;

    @BeforeEach
    void setUp() {
        manager = new SynergyManager();
    }

    private Tower createTowerWithGroup(SynergyGroup group) {
        return new Tower(null, null, new Vector2(0, 0), "Test", 50, 100, 200f, 1.0f, 150, 150, BrainrotTower.TUNG_TUNG) {
            @Override
            public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                               CoinManager coinManager, ProjectileManager projectileManager, com.badlogic.gdx.graphics.Texture pt) {}
            @Override
            public SynergyGroup getSynergyGroup() { return group; }
        };
    }

    @Test
    void evaluateActivatesSynergyWhenThresholdMet() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Array<Tower> towers = new Array<>();
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));

        manager.evaluate(towers);
        assertTrue(manager.isActive(rule));
    }

    @Test
    void evaluateDoesNotActivateWhenThresholdNotMet() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Array<Tower> towers = new Array<>();
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));

        manager.evaluate(towers);
        assertFalse(manager.isActive(rule));
    }

    @Test
    void evaluateDeactivatesWhenTowersRemoved() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Array<Tower> towers = new Array<>();
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));

        manager.evaluate(towers);
        assertTrue(manager.isActive(rule));

        towers.removeIndex(2);
        manager.evaluate(towers);
        assertFalse(manager.isActive(rule));
    }

    @Test
    void evaluateAppliesBoostToTower() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Tower tower = createTowerWithGroup(SynergyGroup.FIRE);
        Array<Tower> towers = new Array<>();
        towers.add(tower);
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));

        manager.evaluate(towers);
        assertTrue(tower.getTotalDamage() > 50);
    }

    @Test
    void evaluateDoesNotApplyBoostToWrongGroup() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Tower physicalTower = createTowerWithGroup(SynergyGroup.PHYSICAL);
        Array<Tower> towers = new Array<>();
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(physicalTower);

        manager.evaluate(towers);
        assertEquals(50, physicalTower.getTotalDamage());
    }

    @Test
    void evaluateRemovesBoostWhenDeactivated() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Tower tower = createTowerWithGroup(SynergyGroup.FIRE);
        Array<Tower> towers = new Array<>();
        towers.add(tower);
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));

        manager.evaluate(towers);
        assertTrue(tower.getTotalDamage() > 50);

        towers.removeIndex(2);
        manager.evaluate(towers);
        assertEquals(50, tower.getTotalDamage());
    }

    @Test
    void evaluateMultipleRules() {
        ElementalSynergy fireRule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        ElementalSynergy supportRule = new ElementalSynergy(SynergyGroup.SUPPORT, 3, new SynergyBoost(1.0f, 2.0f, 1.0f));
        manager.addRule(fireRule);
        manager.addRule(supportRule);

        Array<Tower> towers = new Array<>();
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.SUPPORT));
        towers.add(createTowerWithGroup(SynergyGroup.SUPPORT));
        towers.add(createTowerWithGroup(SynergyGroup.SUPPORT));

        manager.evaluate(towers);
        assertTrue(manager.isActive(fireRule));
        assertTrue(manager.isActive(supportRule));
    }

    @Test
    void evaluateIsIdempotent() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Tower tower = createTowerWithGroup(SynergyGroup.FIRE);
        Array<Tower> towers = new Array<>();
        towers.add(tower);
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));

        manager.evaluate(towers);
        int damageAfterFirst = tower.getTotalDamage();
        manager.evaluate(towers);
        int damageAfterSecond = tower.getTotalDamage();
        assertEquals(damageAfterFirst, damageAfterSecond);
    }

    @Test
    void evaluateHandlesEmptyTowerList() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);

        Array<Tower> towers = new Array<>();
        manager.evaluate(towers);
        assertFalse(manager.isActive(rule));
    }

    @Test
    void evaluateWithMultipleRulesSomeActiveSomeNot() {
        ElementalSynergy fireRule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        ElementalSynergy iceRule = new ElementalSynergy(SynergyGroup.ICE, 3, new SynergyBoost(1.15f));
        manager.addRule(fireRule);
        manager.addRule(iceRule);

        Array<Tower> towers = new Array<>();
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));
        towers.add(createTowerWithGroup(SynergyGroup.FIRE));

        manager.evaluate(towers);
        assertTrue(manager.isActive(fireRule));
        assertFalse(manager.isActive(iceRule));
    }

    @Test
    void addRuleAndIsActive() {
        ElementalSynergy rule = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        manager.addRule(rule);
        assertFalse(manager.isActive(rule));
    }
}
