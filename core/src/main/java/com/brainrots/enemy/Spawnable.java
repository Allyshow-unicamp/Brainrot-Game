package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public interface Spawnable {
    Enemy spawn(Array<Vector2> path);
}
