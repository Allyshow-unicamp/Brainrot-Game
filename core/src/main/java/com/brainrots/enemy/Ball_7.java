package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;

public class Ball_7 extends Enemy implements Spawnable{

    public Ball_7(Array<Vector2> path) {
        
        Texture enemyTexture = GameResourceManager.getGameResourceManager().getTexture(Assets.ENEMY_7);
        super(path, enemyTexture, 50, 150, 100, 100);
    }

    public Enemy spawn(Array<Vector2> path)
    {
        return new Ball_7(path);
    }
}