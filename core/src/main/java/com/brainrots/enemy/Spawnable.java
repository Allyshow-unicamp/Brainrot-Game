package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


/** Interface para criação de inimigos (Prototype). */
public interface Spawnable {
    Enemy spawn(Array<Vector2> path);
}
