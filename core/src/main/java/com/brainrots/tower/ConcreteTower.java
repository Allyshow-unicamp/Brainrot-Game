package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;

public class ConcreteTower extends Tower{

    public ConcreteTower(Texture texture, Vector2 position, String displayName, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, TargetType targetType, int towerID) {
        super(texture, position, displayName, damage, attackSpeed, range, attackCooldown,
             width, height, targetType, towerID);
    }

    @Override
    public Projectile createProjectile(Texture texture, Enemy target) {
        float aoeRadius = getTargetType() == TargetType.AREA ? 150f : 0f;
        return new Projectile(texture, target, this, 75, 75, aoeRadius);
    }

}
