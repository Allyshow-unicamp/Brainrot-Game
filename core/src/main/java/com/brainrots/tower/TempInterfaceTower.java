package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public interface TempInterfaceTower {
    
    public Tower createTower(Vector2 position, int ID);
    
    public Texture getTexture();
}
