package com.brainrots.projectile;

import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;

public class Projectile {
    private Vector2 velocity;
    private int damage;
    private Vector2 position;
    private Vector2 targetPosition;
    private Enemy target;

    public Projectile(Vector2 startPosition, Enemy target, int damage) {
        this.position = new Vector2(startPosition);
        this.target = target;
        this.damage = damage;
        this.targetPosition = new Vector2(target.getBounds().x + target.getBounds().width / 2,
                                         target.getBounds().y + target.getBounds().height / 2);
        this.velocity = new Vector2();
    }

    public void update(float deltaTime) {
        Vector2 direction = new Vector2(targetPosition).sub(position).nor();
        velocity.set(direction.scl(300)); // Example speed
        position.add(velocity.scl(deltaTime / 1000f));

        if (position.dst(targetPosition) < 5f) { 
            target.takeDamage(damage);
        }
    }
}
