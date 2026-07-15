package com.brainrots.engine;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CoinManagerAdditionalTest {

    @Test
    void zeroStartingCoins() {
        CoinManager cm = new CoinManager(0);
        assertEquals(0, cm.getCoins());
    }

    @Test
    void subtractExactAmountLeavesZero() {
        CoinManager cm = new CoinManager(100);
        cm.subtractCoins(100);
        assertEquals(0, cm.getCoins());
    }

    @Test
    void addZeroDoesNothing() {
        CoinManager cm = new CoinManager(100);
        cm.addCoins(0);
        assertEquals(100, cm.getCoins());
    }

    @Test
    void canAffordZeroCost() {
        CoinManager cm = new CoinManager(0);
        assertTrue(cm.canAfford(0));
    }

    @Test
    void subtractLargeAmountDoesNothing() {
        CoinManager cm = new CoinManager(50);
        cm.subtractCoins(999);
        assertEquals(50, cm.getCoins());
    }

    @Test
    void manyAddOperations() {
        CoinManager cm = new CoinManager(0);
        for (int i = 0; i < 100; i++) {
            cm.addCoins(10);
        }
        assertEquals(1000, cm.getCoins());
    }

    @Test
    void alternatingAddSubtract() {
        CoinManager cm = new CoinManager(100);
        cm.subtractCoins(50);
        assertEquals(50, cm.getCoins());
        cm.addCoins(30);
        assertEquals(80, cm.getCoins());
        cm.subtractCoins(80);
        assertEquals(0, cm.getCoins());
        cm.subtractCoins(10);
        assertEquals(0, cm.getCoins());
    }
}
