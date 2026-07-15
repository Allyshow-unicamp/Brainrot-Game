package com.brainrots.tower;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;
import com.brainrots.effect.FireEffect;

import org.junit.jupiter.api.Test;

class ProjectileTest {

    @Test
    void hitDistanceConstant() {
        assertEquals(10f, Projectile.PROJECTILE_HIT_DISTANCE);
    }
}
