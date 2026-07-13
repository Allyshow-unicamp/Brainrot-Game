package com.brainrots.tower;

import com.brainrots.core.Assets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BrainrotTowerTest {

    @Test
    void ballerinaTowerUsesSynergyTextureAsset() {
        assertEquals(Assets.TOWER_BALLERINA, BrainrotTower.BALLERINA_CAPPUCCINA.getSynergyTexture());
    }

    @Test
    void otherTowersHaveNoSynergyTexture() {
        assertNull(BrainrotTower.TUNG_TUNG.getSynergyTexture());
    }
}
