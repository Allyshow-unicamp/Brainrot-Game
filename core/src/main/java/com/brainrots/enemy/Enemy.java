package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.engine.Base;
import com.brainrots.entity.Collidable;
import com.brainrots.entity.GameObject;

public abstract class Enemy extends GameObject implements Collidable {
    protected int hp;
    protected int speed;
    protected int rewardCoins;
    private Array<Vector2> path;
    private int currentWaypointIndex;

    public Enemy(Array<Vector2> path, Texture texture, int speed) {
        this.path = path;
        this.position = path.get(0);
        this.textureRegion = new TextureRegion(texture);

        currentWaypointIndex = 1;
        this.speed = speed;
    }

    public void update(float delta){

        Vector2 vecDist = (new Vector2(path.get(currentWaypointIndex).x, path.get(currentWaypointIndex).y)).sub(position);

        if(currentWaypointIndex < path.size){
            currentWaypointIndex++;

            Vector2 direction = vecDist.nor();

            // System.out.println("going to:" + direction.y + "distance: " + vecDist.len2());

            position.x += direction.x * speed * delta;
            position.y += direction.y * speed * delta;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public boolean intersects(Collidable other) {
        return Intersector.overlaps(this.getBounds(), other.getBounds());
    }

    public int getHp() {
        return hp;
    }
}
