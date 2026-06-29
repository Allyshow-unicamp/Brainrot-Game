package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;

public class TowerProjArea extends Tower {

    private float aoeRadius;

    public TowerProjArea(Texture texture, Vector2 position, String displayName, int damage, int attackSpeed,
            float range, float attackCooldown, int width, int height, int towerID, float aoeRadius) {
        super(texture, position, displayName, damage, attackSpeed, range, attackCooldown, width, height, towerID);
        this.aoeRadius = aoeRadius;
    }

    @Override
    public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                        CoinManager coinManager, ProjectileManager projectileManager, Texture projectileTexture) {
        if (gameTime - getLastAttackTime() < attackCooldown) return;

        Enemy target = findClosestEnemy(activeEnemies);
        if (target != null) {
            projectileManager.spawn(new Projectile(projectileTexture, target, this, 75, 75, aoeRadius));
            attack(gameTime);
        }
    }

    private Enemy findClosestEnemy(Array<Enemy> enemies) {
        float minDist = Float.POSITIVE_INFINITY;
        Enemy closest = null;
        for (Enemy enemy : enemies) {
            float dist = enemy.getPosition().dst(position);
            if (dist <= range && dist < minDist) {
                minDist = dist;
                closest = enemy;
            }
        }
        return closest;
    }
}
