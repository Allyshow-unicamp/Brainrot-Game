package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.effect.StatusEffect;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;

public class TowerArea extends Tower {

    public TowerArea(Texture texture, Texture synergyTexture,Vector2 position, String displayName, int damage,
            float range, float attackCooldown, int width, int height, BrainrotTower towerType) {
        super(texture, synergyTexture,position, displayName, damage, 0, range, attackCooldown, width, height, towerType);
    }

    @Override
    public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                        CoinManager coinManager, ProjectileManager projectileManager, Texture projectileTexture) {
        if (gameTime - getLastAttackTime() < attackCooldown) return;

        StatusEffect effect = createEffect();
        for (Enemy enemy : activeEnemies) {
            if (enemy.getPosition().dst(position) <= range) {
                enemy.takeDamage(getTotalDamage());
                if (effect != null) {
                    enemy.addEffect(effect.copy());
                }
            }
        }
        attack(gameTime);
    }
}
