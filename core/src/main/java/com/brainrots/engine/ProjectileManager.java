package com.brainrots.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.brainrots.effect.StatusEffect;
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
            projIterator.remove(); 
        } 

        //Alvo morreu
        else if (!activeEnemies.contains(projectile.getTarget(), true)) {
            projIterator.remove();
        }
    }
}

    /* Dano splash: atinge inimigos próximos ao impacto, exceto o alvo primário */
    private void applyAreaDamage(Projectile projectile, Array<Enemy> activeEnemies) {
        int damage = projectile.getParent().getTotalDamage();
        float radius = projectile.getAoeRadius();
        StatusEffect effect = projectile.getEffectPrototype();

        for (Enemy enemy : activeEnemies) {
            if (enemy == projectile.getTarget()) continue;
            if (enemy.getPosition().dst(projectile.getPosition()) <= radius) {
                enemy.takeDamage(damage);
                if (effect != null) {
                    enemy.addEffect(effect.copy());
                }
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
