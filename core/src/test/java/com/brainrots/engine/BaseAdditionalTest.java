package com.brainrots.engine;

import static org.junit.jupiter.api.Assertions.*;

import com.badlogic.gdx.math.Vector2;
import com.brainrots.core.GameBalance;

import org.junit.jupiter.api.Test;

class BaseAdditionalTest {

    @Test
    void defaultConstructorUsesBalanceMaxHealth() {
        Base base = new Base(new Vector2(0, 0));
        assertEquals(GameBalance.BASE_MAX_HEALTH, base.getMaxHealth());
        assertEquals(GameBalance.BASE_MAX_HEALTH, base.getHealth());
    }

    @Test
    void getHealthReturnsCurrentHealth() {
        Base base = new Base(500, new Vector2(0, 0));
        assertEquals(500, base.getHealth());
    }

    @Test
    void getMaxHealthReturnsMaxHealth() {
        Base base = new Base(500, new Vector2(0, 0));
        assertEquals(500, base.getMaxHealth());
    }

    @Test
    void getPositionReturnsPosition() {
        Vector2 pos = new Vector2(100, 200);
        Base base = new Base(1000, pos);
        assertEquals(pos, base.getPosition());
    }

    @Test
    void calculateStarsReturns3AtFullHealth() {
        Base base = new Base(1000, new Vector2(0, 0));
        assertEquals(3, base.calculateStars());
    }

    @Test
    void calculateStarsReturns3At66Percent() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(340); // 660/1000 = 66%
        assertEquals(3, base.calculateStars());
    }

    @Test
    void calculateStarsReturns2At33Percent() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(670); // 330/1000 = 33%
        assertEquals(2, base.calculateStars());
    }

    @Test
    void calculateStarsReturns1JustAboveZero() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(999); // 1/1000 = 0.1%
        assertEquals(1, base.calculateStars());
    }

    @Test
    void takeDamageZeroDoesNothing() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(0);
        assertEquals(1000, base.getHealth());
    }

    @Test
    void multipleSmallDamagesAccumulate() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(100);
        base.takeDamage(100);
        base.takeDamage(100);
        assertEquals(700, base.getHealth());
    }

    @Test
    void baseWithOneHealthIsAlive() {
        Base base = new Base(100, new Vector2(0, 0));
        base.takeDamage(99);
        assertTrue(base.hasHealth());
        assertEquals(1, base.getHealth());
    }

    @Test
    void baseWithZeroHealthIsNotAlive() {
        Base base = new Base(1, new Vector2(0, 0));
        base.takeDamage(1);
        assertFalse(base.hasHealth());
    }
}
