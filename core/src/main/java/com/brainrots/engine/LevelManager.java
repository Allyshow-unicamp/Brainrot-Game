package com.brainrots.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.projectile.Projectile;
import com.brainrots.tower.Tower;

public class LevelManager {

    private static final int MAX_HEALTH = 100;
    private static final int MAX_WAVES = 50;
    public static final float ENEMY_BASE_REACH_DISTANCE = 5f;

    private final Map map;
    private final Base base;
    private final Array<Enemy> activeEnemies = new Array<>();
    private final Array<Projectile> activeProjectiles = new Array<>();
    private final Array<Tower> activeTowers = new Array<>();
    private final WaveManager waveManager;
    private final GameResourceManager resourceManager;
    Runnable runOnEnd;

    public LevelManager(GameResourceManager resourceManager, String mapName, Runnable runOnEnd) {
        this.resourceManager = resourceManager;
        this.runOnEnd = runOnEnd;
        map = new Map(mapName, resourceManager);
        base = new Base(MAX_HEALTH, map.getLastPosition());
        waveManager = new WaveManager(MAX_WAVES);
    }

    public Array<Vector2> getPath() {
        return map.getPath();
    }

    public Array<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    public Array<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    public Array<Tower> getActiveTowers() {
        return activeTowers;
    }

    public Texture getMapBackground() {
        return map.getBackground();
    }

    public int getBaseHealth() {
        return base.getHealth();
    }

    public void startNextWave(int nEnemies, String enemy) {
        waveManager.startNextWave(nEnemies, enemy);
    }

    public Enemy closestEnemy(Vector2 position) {
        float minDst = Float.POSITIVE_INFINITY;
        Enemy closest = null;
        for (Enemy enemy : activeEnemies) {
            float dst = enemy.getPosition().dst(position);
            if (dst < minDst) {
                minDst = dst;
                closest = enemy;
            }
        }
        return closest;
    }

    public void update(float delta) {
        updateTowers(delta);
        updateProjectiles(delta);
        waveManager.update(delta, this, resourceManager);
        updateEnemies(delta);
    }

    public void spawnEnemy(Enemy enemy) {
        activeEnemies.add(enemy);
    }

    public void spawnProjectile(Projectile projectile) {
        activeProjectiles.add(projectile);
    }

    public void buyAndPlaceTower(Tower tower) {
        activeTowers.add(tower);
    }

    private void updateTowers(float delta) {
        for (Tower tower : activeTowers) {
            tower.update(delta);
        }
    }

    private void updateProjectiles(float delta) {
        Array<Projectile> toRemove = new Array<>();
        for (Projectile projectile : activeProjectiles) {
            projectile.update(delta);

            if (projectile.reachedTarget()) {
                toRemove.add(projectile);
                continue;
            }

            if (!activeEnemies.contains(projectile.getTarget(), true)) {
                toRemove.add(projectile);
                continue;
            }

            if (projectile.getPosition().dst(projectile.getParent().getPosition()) > projectile.getParent().getRange()) {
                toRemove.add(projectile);
            }
        }
        for (Projectile projectile : toRemove) {
            activeProjectiles.removeValue(projectile, true);
        }
    }

    private void updateEnemies(float delta) {
        Array<Enemy> toRemove = new Array<>();
        for (Enemy enemy : activeEnemies) {
            enemy.update(delta);

            if (enemy.getPosition().dst(base.getPosition()) < ENEMY_BASE_REACH_DISTANCE) {
                if (!base.takeDamage(enemy.getHp())) {
                    runOnEnd.run();
                }
                toRemove.add(enemy);
                continue;
            }

            if (enemy.getHp() <= 0) {
                toRemove.add(enemy);
            }
        }
        for (Enemy enemy : toRemove) {
            activeEnemies.removeValue(enemy, true);
        }
    }
}
