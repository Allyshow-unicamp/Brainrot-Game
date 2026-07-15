package com.brainrots.tower;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProjectileTest {

    @Test
    void hitDistanceConstant() {
        assertEquals(10f, Projectile.PROJECTILE_HIT_DISTANCE);
    }
}
