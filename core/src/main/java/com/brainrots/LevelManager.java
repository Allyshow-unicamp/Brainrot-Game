package com.brainrots;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class LevelManager {

    private int coins;
    private Map map;
    private Array<Enemy> activeEnemies;

    LevelManager(){
        map = new Map("67Hills");

        activeEnemies = new Array<>();

        activeEnemies.add(new Enemy(map.getPath()));
    }

    public Texture getMapBackground(){
        return map.getBackground();
    }

    public Array<Enemy> getEnemies(){
        return activeEnemies;
    }
    
}
