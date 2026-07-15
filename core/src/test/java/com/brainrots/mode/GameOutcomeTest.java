package com.brainrots.mode;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GameOutcomeTest {

    @Test
    void hasThreeValues() {
        assertEquals(3, GameOutcome.values().length);
    }

    @Test
    void valueOfNone() {
        assertEquals(GameOutcome.NONE, GameOutcome.valueOf("NONE"));
    }

    @Test
    void valueOfVictory() {
        assertEquals(GameOutcome.VICTORY, GameOutcome.valueOf("VICTORY"));
    }

    @Test
    void valueOfDefeat() {
        assertEquals(GameOutcome.DEFEAT, GameOutcome.valueOf("DEFEAT"));
    }

    @Test
    void valuesAreDistinct() {
        assertNotEquals(GameOutcome.NONE, GameOutcome.VICTORY);
        assertNotEquals(GameOutcome.NONE, GameOutcome.DEFEAT);
        assertNotEquals(GameOutcome.VICTORY, GameOutcome.DEFEAT);
    }
}
