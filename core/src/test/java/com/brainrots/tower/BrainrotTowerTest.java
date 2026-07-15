package com.brainrots.tower;

import com.brainrots.core.Assets;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BrainrotTowerTest {

    @Test
    void ballerinaTowerUsesSynergyTextureAsset() {
        assertEquals(Assets.TOWER_BALLERINA, BrainrotTower.BALLERINA_CAPPUCCINA.getSynergyTexture());
    }

    @Test
    void tungTungHasSynergyTexture() {
        assertEquals(Assets.TUNG_SYNERGY, BrainrotTower.TUNG_TUNG.getSynergyTexture());
    }
}
