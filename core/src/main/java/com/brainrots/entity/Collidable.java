package com.brainrots.entity;

import com.badlogic.gdx.math.Rectangle;

public interface Collidable {
    public Rectangle getBounds();
    public boolean intersects(Collidable other);
}
