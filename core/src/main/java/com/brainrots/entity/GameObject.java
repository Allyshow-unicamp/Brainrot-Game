package com.brainrots.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    protected Vector2 position;
    protected TextureRegion textureRegion;

    public abstract void update(float deltaTime);

    public void draw(SpriteBatch batch) {
        float x = position.x - (textureRegion.getRegionWidth() / 2f);
        float y = position.y - (textureRegion.getRegionHeight() / 2f);

        batch.draw(textureRegion, x, y);
    }

    public Vector2 getPosition() {
        return position;
    }
}
