package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class EnemyFactory {

    public Enemy spawnEnemy(String enemy, Array<Vector2> path){

        switch (enemy) {
            case "67_Ball":
                return new Ball_67(path);
            case "6_Ball":
                return new Ball_6(path);
            case "7_Ball":
                return new Ball_7(path);
            case "6_7_Ball":
                return new Ball_6_7(path);
            default:
                return null;
        }

    }
    
}
