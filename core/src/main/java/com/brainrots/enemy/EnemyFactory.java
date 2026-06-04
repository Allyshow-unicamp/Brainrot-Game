package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class EnemyFactory {

    public Enemy spawnEnemy(String enemy, Array<Vector2> path){

        switch (enemy) {
            case "67_Ball":
                return new Ball_67(path);
        
            default:
                return null;
        }

    }
    
}
