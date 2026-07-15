package com.brainrots.enemy;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EnemyFactoryTest {

    @Test
    void factoryIsSingleton() {
        EnemyFactory instance1 = EnemyFactory.getEnemyFactory();
        EnemyFactory instance2 = EnemyFactory.getEnemyFactory();
        assertSame(instance1, instance2, "EnemyFactory should be a singleton");
    }

    @Test
    void enemyTemplatesHaveCorrectDefaultValues() {
        EnemyTemplate template67 = new EnemyTemplate("Enemies/67_Ball.png", 175, 190, 100, 100, 125);
        assertEquals(175, template67.getHealth());
        assertEquals(190, template67.getSpeed());
        assertEquals(125, template67.getRewardCoins());

        EnemyTemplate template6 = new EnemyTemplate("Enemies/6_Ball.png", 90, 190, 100, 100, 125);
        assertEquals(90, template6.getHealth());

        EnemyTemplate template7 = new EnemyTemplate("Enemies/7_Ball.png", 45, 280, 100, 100, 125);
        assertEquals(45, template7.getHealth());
        assertEquals(280, template7.getSpeed());

        EnemyTemplate template67Boss = new EnemyTemplate("Enemies/6+7_Ball.png", 850, 95, 100, 100, 300);
        assertEquals(850, template67Boss.getHealth());
        assertEquals(300, template67Boss.getRewardCoins());
    }
}
