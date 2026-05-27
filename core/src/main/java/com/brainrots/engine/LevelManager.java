package com.brainrots.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.ConcreteEnemy;
import com.brainrots.enemy.Enemy;

public class LevelManager {

    private int coins;
    private Map map;
    private Array<Enemy> activeEnemies;

    public LevelManager(){
        map = new Map("67Hills");

        activeEnemies = new Array<>();

        activeEnemies.add(new ConcreteEnemy(map.getPath(), GameResourceManager.getTexture("temp.png")));
    }

    public Texture getMapBackground(){
        return map.getBackground();
    }

    public Array<Enemy> getEnemies(){
        return activeEnemies;
    }

}
