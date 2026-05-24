package com.brainrots;

import com.badlogic.gdx.graphics.Texture;

public class LevelManager {

    private int coins;
    private Map map;

    LevelManager(){
        map = new Map("67Hills");
    }

    public Texture getMapBackground(){
        return map.getBackground();
    }

    

    
}
