package com.brainrots.tower;

import java.util.Vector;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.MainUX.UpgradeUX.TowerUpgradeMenu;
import com.brainrots.enemy.Enemy;

public class ConcreteTower extends Tower{


    public ConcreteTower(Texture texture, Vector2 position, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, TargetType targetType, int towerID) {
        super(texture, position, damage, attackSpeed, range, attackCooldown,
             width, height, targetType,1,towerID);
    }

    @Override
    public Projectile createProjectile(Texture texture, Enemy target) {
        return new Projectile(texture, target, this, 75, 75);
    }


}
