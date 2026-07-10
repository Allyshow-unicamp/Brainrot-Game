package com.brainrots.engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CoinManagerTest {

    @Test
    void startsWithGivenCoins() {
        CoinManager cm = new CoinManager(300);
        assertEquals(300, cm.getCoins());
    }

    @Test
    void canAffordReturnsTrueWhenEnoughCoins() {
        CoinManager cm = new CoinManager(100);
        assertTrue(cm.canAfford(50));
    }

    @Test
    void canAffordReturnsFalseWhenNotEnough() {
        CoinManager cm = new CoinManager(30);
        assertFalse(cm.canAfford(50));
    }

    @Test
    void canAffordReturnsTrueWhenExactlyEnough() {
        CoinManager cm = new CoinManager(50);
        assertTrue(cm.canAfford(50));
    }

    @Test
    void subtractCoinsReducesBalance() {
        CoinManager cm = new CoinManager(100);
        cm.subtractCoins(40);
        assertEquals(60, cm.getCoins());
    }

    @Test
    void subtractCoinsDoesNotGoBelowZero() {
        CoinManager cm = new CoinManager(10);
        cm.subtractCoins(100);
        assertEquals(10, cm.getCoins());
    }

    @Test
    void addCoinsIncreasesBalance() {
        CoinManager cm = new CoinManager(50);
        cm.addCoins(30);
        assertEquals(80, cm.getCoins());
    }

    @Test
    void multipleOperationsMaintainCorrectBalance() {
        CoinManager cm = new CoinManager(200);
        cm.subtractCoins(50);
        cm.addCoins(100);
        cm.subtractCoins(30);
        assertEquals(220, cm.getCoins());
    }
}
