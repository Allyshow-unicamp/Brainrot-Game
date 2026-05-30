package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.entity.GameObject;

public abstract class Enemy extends GameObject {
    protected int hp;
    protected int speed;
    protected int rewardCoins;
    private Array<Vector2> path;
    private int currentWaypointIndex;

    public Enemy(Array<Vector2> path, Texture texture, int health, int speed, int width, int height) {
        this.path = path;
        this.position = new Vector2(path.get(0));
        this.textureRegion = new TextureRegion(texture);
        this.hp = health;
        this.width = width;
        this.height = height;

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

    public boolean takeDamage(int damage) {
        hp -= damage;
        return hp <= 0;
    }

    public int getHp() {
        return hp;
    }
}
