package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;
import com.badlogic.gdx.utils.Array;


public interface Spawnable {

    /**
     * Metodo para chamar o construtor
     * <p>Acompanha o EneyFactory, que eh o invoker dos enemies</p>
     */
    public Enemy spawn(Array<Vector2> path);
}
