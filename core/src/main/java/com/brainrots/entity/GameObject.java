package com.brainrots.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {
    protected Vector2 position;
    protected TextureRegion textureRegion;
    protected int width;
    protected int height;

    public abstract void update(float deltaTime);

    public void draw(SpriteBatch batch) {
        float x = position.x - width / 2f;
        float y = position.y - height / 2f;

        batch.draw(textureRegion, x, y, width, height);
    }

    public Vector2 getPosition() {
        return position;
    }
}
