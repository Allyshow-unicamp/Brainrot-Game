package com.brainrots.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.brainrots.enemy.Enemy;
import com.brainrots.tower.Projectile;

public class ProjectileManager {

    private Array<Projectile> activeProjectiles;

    public ProjectileManager() {
        this.activeProjectiles = new Array<>();
    }

    public void spawn(Projectile projectile) {
        activeProjectiles.add(projectile);
    }

    public void update(float delta, Array<Enemy> activeEnemies) {
        ArrayIterator<Projectile> projIterator = activeProjectiles.iterator();

        while (projIterator.hasNext()) {
            Projectile projectile = projIterator.next();

            projectile.update(delta);

            if (projectile.reachedTarget()) {
                if (projectile.isAreaOfEffect()) {
                    applyAreaDamage(projectile, activeEnemies);
                }
                activeProjectiles.removeValue(projectile, false);
            } else if (!activeEnemies.contains(projectile.getTarget(), true) ||
             projectile.getPosition().dst(projectile.getParent().getPosition()) > projectile.getParent().getRange()) {
                activeProjectiles.removeValue(projectile, false);
            }
        }
    }

    private void applyAreaDamage(Projectile projectile, Array<Enemy> activeEnemies) {
        int damage = projectile.getParent().getTotalDamage();
        float radius = projectile.getAoeRadius();

        for (Enemy enemy : activeEnemies) {
            if (enemy == projectile.getTarget()) continue;
            if (enemy.getPosition().dst(projectile.getPosition()) <= radius) {
                enemy.takeDamage(damage);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (Projectile projectile : activeProjectiles) {
            projectile.draw(batch);
        }
    }

    public Array<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }
}
