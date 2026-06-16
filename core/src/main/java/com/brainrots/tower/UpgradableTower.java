package com.brainrots.tower;

import com.badlogic.gdx.math.Vector2;

public interface UpgradableTower {
    
    public abstract Tower createTower(Vector2 position, int ID);

}
