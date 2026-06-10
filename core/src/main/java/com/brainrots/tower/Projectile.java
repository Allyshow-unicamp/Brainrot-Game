package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;
import com.brainrots.entity.GameObject;

public class Projectile extends GameObject {
    private Vector2 targetPosition;
    private Enemy target;
    private boolean reachedTarget = false;
    private Vector2 mathVector = new Vector2();
    private Tower parent;

    public static final float PROJECTILE_HIT_DISTANCE = 10f;

    public Projectile(Texture texture, Enemy target, Tower parent, int width, int height) {
        this.textureRegion = new TextureRegion(texture);
        this.position = new Vector2(parent.getPosition());
        this.width = width;
        this.height = height;
        this.target = target;
        this.parent = parent;
        this.targetPosition = new Vector2(target.getBounds().x + target.getBounds().width / 2f,
                target.getBounds().y + target.getBounds().height / 2f);
    }

    public void update(float deltaTime) {
        if (!reachedTarget) {
            targetPosition.set(target.getPosition());

            mathVector.set(targetPosition).sub(position).nor();

            position.add(mathVector.scl(parent.getAttackSpeed() * deltaTime));

            if (position.dst(targetPosition) < PROJECTILE_HIT_DISTANCE) {
                target.takeDamage(parent.getDamage());
                reachedTarget = true;
            }
        }
    }

    public boolean reachedTarget() {
        return reachedTarget;
    }

    public Enemy getTarget() {
        return target;
    }

    public Tower getParent() {
        return parent;
    }
}
