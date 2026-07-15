package com.brainrots.synergy;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.utils.Array;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SynergyManagerTest {

    private SynergyManager manager;

    @BeforeEach
    void setUp() {
        manager = new SynergyManager();
    }

    @Test
    void emptyManagerHasNoActiveRules() {
        assertTrue(manager.getActiveRules().isEmpty());
    }

    @Test
    void elementalSynergyActivatesWithEnoughTowers() {
        ElementalSynergy synergy = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));

        Array<TowerInfo> towers = new Array<>();
        towers.add(new TowerInfo(SynergyGroup.FIRE));
        towers.add(new TowerInfo(SynergyGroup.FIRE));
        towers.add(new TowerInfo(SynergyGroup.FIRE));

        assertTrue(synergy.isActivated(convert(towers)));
    }

    @Test
    void elementalSynergyDoesNotActivateWithFewTowers() {
        ElementalSynergy synergy = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));

        Array<TowerInfo> towers = new Array<>();
        towers.add(new TowerInfo(SynergyGroup.FIRE));
        towers.add(new TowerInfo(SynergyGroup.FIRE));

        assertFalse(synergy.isActivated(convert(towers)));
    }

    @Test
    void elementalSynergyIgnoresOtherGroups() {
        ElementalSynergy synergy = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));

        Array<TowerInfo> towers = new Array<>();
        towers.add(new TowerInfo(SynergyGroup.FIRE));
        towers.add(new TowerInfo(SynergyGroup.PHYSICAL));
        towers.add(new TowerInfo(SynergyGroup.SUPPORT));

        assertFalse(synergy.isActivated(convert(towers)));
    }

    @Test
    void elementalSynergyExactThreshold() {
        ElementalSynergy synergy = new ElementalSynergy(SynergyGroup.PHYSICAL, 2, new SynergyBoost(1.10f));

        Array<TowerInfo> towers = new Array<>();
        towers.add(new TowerInfo(SynergyGroup.PHYSICAL));
        towers.add(new TowerInfo(SynergyGroup.PHYSICAL));

        assertTrue(synergy.isActivated(convert(towers)));
    }

    @Test
    void synergyBoostMultipliersAreStored() {
        SynergyBoost boost = new SynergyBoost(1.15f, 1.5f, 0.85f);
        assertEquals(1.15f, boost.getDamageMultiplier(), 0.001f);
        assertEquals(1.5f, boost.getRangeMultiplier(), 0.001f);
        assertEquals(0.85f, boost.getCooldownMultiplier(), 0.001f);
    }

    @Test
    void synergyBoostDefaultCooldownAndRange() {
        SynergyBoost boost = new SynergyBoost(1.20f);
        assertEquals(1.20f, boost.getDamageMultiplier(), 0.001f);
        assertEquals(1.0f, boost.getRangeMultiplier(), 0.001f);
        assertEquals(1.0f, boost.getCooldownMultiplier(), 0.001f);
    }

    @Test
    void synergyGroupEnumValues() {
        assertEquals(4, SynergyGroup.values().length);
        assertNotNull(SynergyGroup.valueOf("FIRE"));
        assertNotNull(SynergyGroup.valueOf("ICE"));
        assertNotNull(SynergyGroup.valueOf("PHYSICAL"));
        assertNotNull(SynergyGroup.valueOf("SUPPORT"));
    }

    @Test
    void synergyRuleName() {
        ElementalSynergy synergy = new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f));
        assertEquals("FIRE Synergy (3+)", synergy.getName());
        assertEquals(SynergyGroup.FIRE, synergy.getGroup());
    }

    /**
     * Test double mínimo que expõe apenas o SynergyGroup.
     * Não é uma Tower real — serve apenas para testar isActivated().
     */
    private static class TowerInfo {
        final SynergyGroup group;
        TowerInfo(SynergyGroup g) { this.group = g; }
    }

    /**
     * Converte TowerInfo[] para Array<com.brainrots.tower.Tower>
     * usando um wrapper mínimo que não depende de Texture.
     */
    private Array<com.brainrots.tower.Tower> convert(Array<TowerInfo> infos) {
        Array<com.brainrots.tower.Tower> towers = new Array<>();
        for (TowerInfo info : infos) {
            towers.add(new com.brainrots.tower.Tower(null,null ,null, "test", 0, 0, 0f, 0f, 0, 0, null) {
                @Override
                public void update(float gt, Array<com.brainrots.enemy.Enemy> ae,
                                   Array<com.brainrots.tower.Tower> at,
                                   com.brainrots.engine.CoinManager cm,
                                   com.brainrots.engine.ProjectileManager pm,
                                   com.badlogic.gdx.graphics.Texture pt) {}
                @Override
                public SynergyGroup getSynergyGroup() { return info.group; }
            });
        }
        return towers;
    }
}
