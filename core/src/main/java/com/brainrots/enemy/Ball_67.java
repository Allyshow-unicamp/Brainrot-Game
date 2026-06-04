package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;

public class Ball_67 extends Enemy{

    public Ball_67(Array<Vector2> path) {
        
        Texture enemyTexture = GameResourceManager.getGameResourceManager().getTexture(Assets.ENEMY_TEMP);
        super(path, enemyTexture, 100, 200, 100, 100);
    }

}