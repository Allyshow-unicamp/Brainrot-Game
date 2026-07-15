package com.brainrots.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/** Entidade abstrata com posição, textura e detecção de colisão/clique. */
public abstract class GameObject implements Collidable {
    protected Vector2 position;
    protected TextureRegion textureRegion;
    protected int width;
    protected int height;

    public void draw(SpriteBatch batch) {
        float x = position.x - width / 2f;
        float y = position.y - height / 2f;

        batch.draw(textureRegion, x, y, width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(position.x - width / 2f, position.y - height / 2f, width, height);
    }

    @Override
    public boolean intersects(Collidable other) {
        return Intersector.overlaps(getBounds(), other.getBounds());
    }

    public Vector2 getPosition() {
        return position;
    }

     public boolean wasClicked(float mouseXCoord, float mouseYCoord)
    {
        //Determinar area da colisao
        float initialXCoord = position.x - width / 2f;
        float finalXCoord = initialXCoord + width;
        float initialYCoord = position.y;
        float finalYCoord = position.y + height;

        return(onXRange(initialXCoord, finalXCoord, mouseXCoord) && 
        onYRange(initialYCoord, finalYCoord, mouseYCoord));
    }

    private boolean onXRange(float initialXCoord, float finalXCoord, float currentXCoord)
    {
        return(currentXCoord < finalXCoord && currentXCoord > initialXCoord);
    }

    private boolean onYRange(float initialYCoord, float finalYCoord, float currentYCoord)
    {
        return(currentYCoord < finalYCoord && currentYCoord > initialYCoord);
    }  
}
