package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.entity.Collidable;
import com.brainrots.entity.GameObject;

public abstract class Enemy extends GameObject implements Collidable {
    protected int hp;
    protected int speed;
    protected int rewardCoins;
    private Array<Vector2> path;
    private int currentWaypointIndex;

    public Enemy(Array<Vector2> path, Texture texture, int health, int speed) {
        this.path = path;
        this.position = new Vector2(path.get(0));
        this.textureRegion = new TextureRegion(texture);
        this.hp = health;

        currentWaypointIndex = 1;
        this.speed = speed;
    }

    public void update(float delta) {
        if (currentWaypointIndex < path.size) {
            Vector2 target = path.get(currentWaypointIndex);
            float moveStep = speed * delta;
            float distanceToTarget = position.dst(target);

            if (distanceToTarget <= moveStep) {
                position.set(target);
                currentWaypointIndex++;
            } else {
                Vector2 direction = new Vector2(target).sub(position).nor();
                position.add(direction.scl(moveStep));
            }
        }
    }

    public Rectangle getBounds() {
        int height = textureRegion.getRegionHeight();
        int width = textureRegion.getRegionWidth();
        return new Rectangle(position.x - width / 2, position.y - height / 2, width, height);
    }

    public boolean intersects(Collidable other) {
        return Intersector.overlaps(this.getBounds(), other.getBounds());
    }

    public int getHp() {
        return hp;
    }
}
