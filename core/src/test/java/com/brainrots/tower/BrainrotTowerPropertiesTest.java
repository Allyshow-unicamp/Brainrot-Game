package com.brainrots.tower;

import static org.junit.jupiter.api.Assertions.*;

import com.brainrots.core.Assets;
import com.brainrots.core.GameBalance;
import com.brainrots.synergy.SynergyGroup;

import org.junit.jupiter.api.Test;

class BrainrotTowerPropertiesTest {

    @Test
    void tungTungHasCorrectProperties() {
        BrainrotTower t = BrainrotTower.TUNG_TUNG;
        assertEquals("tung_tung", t.getTowerId());
        assertEquals("Tung Tung Tung Sahur", t.getDisplayName());
        assertEquals(GameBalance.TOWER_TUNG_DAMAGE, t.getDamage());
        assertEquals(GameBalance.TOWER_TUNG_COST, t.getCost());
        assertEquals(GameBalance.TOWER_TUNG_RANGE, t.getRange(), 0.001f);
        assertEquals(GameBalance.TOWER_TUNG_COOLDOWN, t.getAttackCooldown(), 0.001f);
        assertEquals(SynergyGroup.PHYSICAL, t.getSynergyGroup());
        assertEquals(Assets.TUNG_SYNERGY, t.getSynergyTexture());
    }

    @Test
    void bombardiroHasCorrectProperties() {
        BrainrotTower t = BrainrotTower.BOMBARDILO_CROCODILO;
        assertEquals("bombardiro_crocodilo", t.getTowerId());
        assertEquals(GameBalance.TOWER_BOMB_DAMAGE, t.getDamage());
        assertEquals(GameBalance.TOWER_BOMB_COST, t.getCost());
        assertEquals(GameBalance.TOWER_BOMB_RANGE, t.getRange(), 0.001f);
        assertEquals(SynergyGroup.FIRE, t.getSynergyGroup());
    }

    @Test
    void nuclearoHasCorrectProperties() {
        BrainrotTower t = BrainrotTower.NUCLEARO_DINOSAURO;
        assertEquals("nuclearo_dinosauro", t.getTowerId());
        assertEquals(GameBalance.TOWER_NUC_DAMAGE, t.getDamage());
        assertEquals(GameBalance.TOWER_NUC_COST, t.getCost());
        assertEquals(SynergyGroup.FIRE, t.getSynergyGroup());
    }

    @Test
    void cappuccinoHasCorrectProperties() {
        BrainrotTower t = BrainrotTower.CAPPUCCINO_ASSASSINO;
        assertEquals("cappuccino_assassino", t.getTowerId());
        assertEquals(0, t.getDamage());
        assertEquals(GameBalance.TOWER_COIN_COST, t.getCost());
        assertEquals(SynergyGroup.SUPPORT, t.getSynergyGroup());
    }

    @Test
    void ballerinaHasCorrectProperties() {
        BrainrotTower t = BrainrotTower.BALLERINA_CAPPUCCINA;
        assertEquals("ballerina_cappuccina", t.getTowerId());
        assertEquals(0, t.getDamage());
        assertEquals(GameBalance.TOWER_BOOST_COST, t.getCost());
        assertEquals(SynergyGroup.SUPPORT, t.getSynergyGroup());
    }

    @Test
    void fireTowersHaveFireEffectPrototype() {
        assertNotNull(BrainrotTower.BOMBARDILO_CROCODILO.createEffectPrototype());
        assertNotNull(BrainrotTower.NUCLEARO_DINOSAURO.createEffectPrototype());
    }

    @Test
    void nonFireTowersReturnNullEffect() {
        assertNull(BrainrotTower.TUNG_TUNG.createEffectPrototype());
        assertNull(BrainrotTower.CAPPUCCINO_ASSASSINO.createEffectPrototype());
        assertNull(BrainrotTower.BALLERINA_CAPPUCCINA.createEffectPrototype());
    }

    @Test
    void createEffectPrototypeReturnsNewInstance() {
        var e1 = BrainrotTower.BOMBARDILO_CROCODILO.createEffectPrototype();
        var e2 = BrainrotTower.BOMBARDILO_CROCODILO.createEffectPrototype();
        assertNotSame(e1, e2);
    }

    @Test
    void allFiveTowersExist() {
        assertEquals(5, BrainrotTower.values().length);
    }

    @Test
    void tungTungTexture() {
        assertEquals(Assets.TOWER_TUNG, BrainrotTower.TUNG_TUNG.getTexture());
    }

    @Test
    void bombardiroTexture() {
        assertEquals(Assets.TOWER_BOMBARDILO_CROCODILO, BrainrotTower.BOMBARDILO_CROCODILO.getTexture());
    }

    @Test
    void nuclearoTexture() {
        assertEquals(Assets.TOWER_NUCLEARO_DINOSAURO, BrainrotTower.NUCLEARO_DINOSAURO.getTexture());
    }

    @Test
    void cappuccinoTexture() {
        assertEquals(Assets.TOWER_CAPPUCCINO, BrainrotTower.CAPPUCCINO_ASSASSINO.getTexture());
    }

    @Test
    void ballerinaTexture() {
        assertEquals(Assets.TOWER_CAPPUCCINA, BrainrotTower.BALLERINA_CAPPUCCINA.getTexture());
    }

    @Test
    void tungTungCost() {
        assertEquals(150, BrainrotTower.TUNG_TUNG.getCost());
    }

    @Test
    void bombardiroCost() {
        assertEquals(300, BrainrotTower.BOMBARDILO_CROCODILO.getCost());
    }

    @Test
    void nuclearoCost() {
        assertEquals(450, BrainrotTower.NUCLEARO_DINOSAURO.getCost());
    }

    @Test
    void cappuccinoCost() {
        assertEquals(600, BrainrotTower.CAPPUCCINO_ASSASSINO.getCost());
    }

    @Test
    void ballerinaCost() {
        assertEquals(750, BrainrotTower.BALLERINA_CAPPUCCINA.getCost());
    }

    @Test
    void bombardiroSynergyTexture() {
        assertEquals(Assets.BOMBARDILO_SYNERGY, BrainrotTower.BOMBARDILO_CROCODILO.getSynergyTexture());
    }

    @Test
    void nuclearoSynergyTexture() {
        assertEquals(Assets.NUCLEARO_SYNERGY, BrainrotTower.NUCLEARO_DINOSAURO.getSynergyTexture());
    }

    @Test
    void cappuccinoSynergyTexture() {
        assertEquals(Assets.CAPPUCCINO_SYNERGY, BrainrotTower.CAPPUCCINO_ASSASSINO.getSynergyTexture());
    }

    @Test
    void tungTungSynergyTexture() {
        assertEquals(Assets.TUNG_SYNERGY, BrainrotTower.TUNG_TUNG.getSynergyTexture());
    }

    @Test
    void descriptionsAreNotEmpty() {
        for (BrainrotTower t : BrainrotTower.values()) {
            assertNotNull(t.getDescription());
            assertFalse(t.getDescription().isEmpty());
        }
    }

    @Test
    void allTowerWidthsAndHeights() {
        for (BrainrotTower t : BrainrotTower.values()) {
            assertEquals(150, t.getWidth());
            assertEquals(150, t.getHeight());
        }
    }

    @Test
    void extraValueForCoinAndBoost() {
        assertEquals(GameBalance.TOWER_COIN_PER_TOWER, BrainrotTower.CAPPUCCINO_ASSASSINO.getExtraValue());
        assertEquals(GameBalance.TOWER_BOOST_AMOUNT, BrainrotTower.BALLERINA_CAPPUCCINA.getExtraValue());
        assertEquals(0, BrainrotTower.TUNG_TUNG.getExtraValue());
    }
}
