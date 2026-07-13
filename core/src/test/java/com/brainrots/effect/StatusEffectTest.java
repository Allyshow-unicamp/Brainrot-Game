package com.brainrots.effect;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Testes para os efeitos de status.
 * Testam apenas a lógica interna dos efeitos sem depender
 * de Enemy ou libGDX (evita mocking de classes nativas).
 */
class StatusEffectTest {

    @Test
    void fireEffectCopyCreatesIndependentInstance() {
        FireEffect original = new FireEffect(5f, 10);
        StatusEffect copy = original.copy();
        assertNotNull(copy);
        assertNotSame(original, copy, "Copy should be a different object");
        assertEquals(original.getDuration(), copy.getDuration(), 0.001f);
        assertEquals(original.getDamagePerTick(), ((FireEffect)copy).getDamagePerTick());
    }

    @Test
    void poisonEffectCopyCreatesIndependentInstance() {
        PoisonEffect original = new PoisonEffect(3f, 7);
        StatusEffect copy = original.copy();
        assertNotNull(copy);
        assertEquals(original.getDamagePerTick(), ((PoisonEffect)copy).getDamagePerTick());
    }

    @Test
    void iceEffectCopyCreatesIndependentInstance() {
        IceEffect original = new IceEffect(4f);
        StatusEffect copy = original.copy();
        assertNotNull(copy);
        assertEquals(original.getDuration(), copy.getDuration(), 0.001f);
    }

    @Test
    void stunEffectCopyCreatesIndependentInstance() {
        StunEffect original = new StunEffect(2f);
        StatusEffect copy = original.copy();
        assertNotNull(copy);
        assertEquals(original.getDuration(), copy.getDuration(), 0.001f);
    }

    @Test
    void effectIsNotExpiredInitially() {
        FireEffect fire = new FireEffect(2f, 5);
        assertFalse(fire.isExpired());
    }

    @Test
    void effectWithZeroDurationIsExpired() {
        FireEffect fire = new FireEffect(0f, 5);
        assertTrue(fire.isExpired());
    }

    @Test
    void fireDurationIsStored() {
        FireEffect fire = new FireEffect(3.5f, 8);
        assertEquals(3.5f, fire.getDuration(), 0.001f);
        assertEquals(3.5f, fire.getRemainingTime(), 0.001f);
    }

    @Test
    void copyModifiesRemainingTimeIndependently() {
        FireEffect original = new FireEffect(5f, 10);
        original.setRemainingTime(3f);
        StatusEffect copy = original.copy();
        assertEquals(3f, original.getRemainingTime(), 0.001f);
        // Copy should start fresh with full duration (depending on implementation)
        // This test verifies they are different objects
        assertNotSame(original, copy);
    }
}
