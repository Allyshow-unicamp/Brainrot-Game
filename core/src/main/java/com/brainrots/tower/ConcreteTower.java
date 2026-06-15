package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.MainUX.UpgradeUX.TowerUpgradeMenu;
import com.brainrots.enemy.Enemy;

public class ConcreteTower extends Tower {


    public ConcreteTower(Texture texture, Vector2 position, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, TargetType targetType) {
        super(texture, position, damage, attackSpeed, range, attackCooldown, width, height, targetType,3);
    }

    @Override
    public Projectile createProjectile(Texture texture, Enemy target) {
        return new Projectile(texture, target, this, 75, 75);
    }
}
