package com.brainrots.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;
import com.brainrots.entity.Collidable;
import com.brainrots.entity.GameObject;
import com.brainrots.tower.Tower;

public class Projectile extends GameObject implements Collidable {
    private Vector2 targetPosition;
    private Enemy target;
    private boolean reachedTarget = false;
    private Vector2 mathVector = new Vector2();
    private Tower parent;

    public Projectile(Texture texture, Enemy target, Tower parent, int width, int height) {
        this.textureRegion = new TextureRegion(texture);
        this.position = new Vector2(parent.getPosition());
        this.width = width;
        this.height = height;
        this.target = target;
        this.parent = parent;
        this.targetPosition = new Vector2(target.getBounds().x + target.getBounds().width / 2,
                                         target.getBounds().y + target.getBounds().height / 2);
    }

    public void update(float deltaTime) {
        if (!reachedTarget) {
            targetPosition.set(target.getPosition());

            mathVector.set(targetPosition).sub(position).nor();

            position.add(mathVector.scl(parent.getAttackSpeed() * deltaTime));

            if (position.dst(targetPosition) < 5f) {
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

    @Override
    public Rectangle getBounds() {
        return new Rectangle(position.x - width / 2, position.y - height / 2, width, height);
    }

    @Override
    public boolean intersects(Collidable other) {
        return Intersector.overlaps(this.getBounds(), other.getBounds());
    }
}
