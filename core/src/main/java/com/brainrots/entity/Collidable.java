package com.brainrots.entity;

import com.badlogic.gdx.math.Rectangle;

/** Interface para detecção de colisão entre entidades do jogo. */
public interface Collidable {
    public Rectangle getBounds();
    public boolean intersects(Collidable other);
}
