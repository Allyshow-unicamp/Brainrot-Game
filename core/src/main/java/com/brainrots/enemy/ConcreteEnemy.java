package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ConcreteEnemy extends Enemy {

    public ConcreteEnemy(Array<Vector2> path, Texture texture, int speed) {
        super(path, texture, speed);
    }
}
