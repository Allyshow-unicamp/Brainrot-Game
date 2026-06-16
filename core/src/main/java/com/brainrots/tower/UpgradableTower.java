package com.brainrots.tower;

import java.util.Vector;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public interface UpgradableTower {
    
    public abstract Tower createTower(Vector2 position, int ID);

}
