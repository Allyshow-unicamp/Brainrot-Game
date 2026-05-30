package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public final class EnemyFactory {
    private static final int DEFAULT_HP = 50;
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 100;

    public static Enemy createDefault(Array<Vector2> path, Texture texture, int waveNumber) {
        int speed = waveNumber * 500;
        return new ConcreteEnemy(path, texture, DEFAULT_HP, speed, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
