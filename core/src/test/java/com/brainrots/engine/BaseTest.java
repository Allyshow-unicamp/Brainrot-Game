package com.brainrots.engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Vector2;

class BaseTest {

    @Test
    void startsWithFullHealth() {
        Base base = new Base(1000, new Vector2(0, 0));
        assertEquals(1000, base.getHealth());
        assertTrue(base.hasHealth());
    }

    @Test
    void takeDamageReducesHealth() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(300);
        assertEquals(700, base.getHealth());
    }

    @Test
    void takeDamageDoesNotGoBelowZero() {
        Base base = new Base(100, new Vector2(0, 0));
        base.takeDamage(200);
        assertEquals(0, base.getHealth());
        assertFalse(base.hasHealth());
    }

    @Test
    void calculateStarsReturns3WhenAbove66Percent() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(300); // 700/1000 = 70% > 66%
        assertEquals(3, base.calculateStars());
    }

    @Test
    void calculateStarsReturns2WhenBetween33And66Percent() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(500); // 500/1000 = 50%
        assertEquals(2, base.calculateStars());
    }

    @Test
    void calculateStarsReturns1WhenBelow33Percent() {
        Base base = new Base(1000, new Vector2(0, 0));
        base.takeDamage(800); // 200/1000 = 20%
        assertEquals(1, base.calculateStars());
    }
}
