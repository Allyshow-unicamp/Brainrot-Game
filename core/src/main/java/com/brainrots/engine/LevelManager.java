package com.brainrots.engine;

import java.util.Queue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.projectile.Projectile;
import com.brainrots.screen.ScreenManager;
import com.brainrots.screen.ScreenManager.ScreenType;
import com.brainrots.tower.Tower;

public class LevelManager {

    private final int maxHealth = 100;
    private final int maxWaves = 50;

    private int coins;
    private Map map;
    private Array<Enemy> activeEnemies;
    private Array<Projectile> activeProjectiles;
    private Array<Tower> activeTowers;
    private Base base;
    private WaveManager waveManager;
    private GameResourceManager resourceManager;
    private ScreenManager screenManager;

    public Array<Vector2> getPath() {
        return map.getPath();
    }

    public Array<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    public int getBaseHealth() {
        return base.getHealth();
    }

    public LevelManager(GameResourceManager resourceManager, ScreenManager screenManager, String mapName){
        this.resourceManager = resourceManager;
        this.screenManager = screenManager;
        map = new Map(mapName, resourceManager);
        base = new Base(maxHealth, map.getLastPosition());
        activeEnemies = new Array<>();
        activeProjectiles = new Array<>();
        activeTowers = new Array<>();
        waveManager = new WaveManager(maxWaves);
    }

    public void startNextWave(Queue<String> enemiesToSpawn) {
        waveManager.startNextWave(enemiesToSpawn);
    }

    public Enemy closestEnemy(Vector2 position) {
        float minDst = Float.POSITIVE_INFINITY;
        Enemy minEnemy = null;
        for (Enemy enemy : activeEnemies) {
            float dst = enemy.getPosition().dst(position);
            if (dst < minDst) {
                minDst = dst;
                minEnemy = enemy;
            }
        }
        return minEnemy;
    }

    public void update(float delta) {
        for (Tower tower : activeTowers) {
            tower.update(delta);
        }

        Array<Projectile> projectilesToBeRemoved = new Array<>();
        for (Projectile projectile : activeProjectiles) {
            projectile.update(delta);

            if (projectile.reachedTarget())
                projectilesToBeRemoved.add(projectile);

            // inimigo que já morreu
            if (!activeEnemies.contains(projectile.getTarget(), true))
                projectilesToBeRemoved.add(projectile);

            if (projectile.getPosition().dst(projectile.getParent().getPosition()) > projectile.getParent().getRange())
                projectilesToBeRemoved.add(projectile);
        }
        for (Projectile projectile : projectilesToBeRemoved) {
            activeProjectiles.removeValue(projectile, true);
        }

        waveManager.update(delta, this, resourceManager);

        Array<Enemy> enemiesToBeRemoved = new Array<>();
        for(Enemy enemy : activeEnemies){
            enemy.update(delta);

            if (enemy.getPosition().dst(base.getPosition()) < 5.0f) {
                boolean alive = base.takeDamage(enemy.getHp());
                if (!alive) {
                    screenManager.switchScreen(ScreenType.MAIN_MENU);
                }
                enemiesToBeRemoved.add(enemy);
            }

            if (enemy.getHp() <= 0)
                enemiesToBeRemoved.add(enemy);
        }
        for (Enemy enemy : enemiesToBeRemoved){
            activeEnemies.removeValue(enemy, true);
        }
    }

    public void spawnEnemy(Enemy enemy) {
        activeEnemies.add(enemy);
    }

    public void spawnProjectile(Projectile projectile) {
        activeProjectiles.add(projectile);
    }

    public void draw(SpriteBatch batch) {
        batch.begin();

        Texture background = map.getBackground();
        batch.draw(background, 0, 0);

        for(Enemy enemy : activeEnemies){
            enemy.draw(batch);
        }

        for(Tower tower : activeTowers) {
            tower.draw(batch);
        }

        for(Projectile projectile: activeProjectiles) {
            projectile.draw(batch);
        }

        batch.end();
    }

    public void buyAndPlaceTower(Tower tower) {
        activeTowers.add(tower);
    }
}
