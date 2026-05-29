package com.brainrots.tower;

import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;
import com.brainrots.projectile.Projectile;

public class ConcreteTower extends Tower {

    public ConcreteTower(Vector2 position, int damage, float range, float attackCooldown, TargetType targetType) {
        super(position, damage, range, attackCooldown, targetType);
    }

    @Override
    public Projectile createProjectile(Enemy target) {
        return new Projectile(position, target, damage);
    }
}
