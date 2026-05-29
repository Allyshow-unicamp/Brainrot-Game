package com.brainrots.engine;

import java.util.Queue;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.screen.ScreenManager;
import com.brainrots.screen.ScreenManager.ScreenType;

public class LevelManager {

    private final int maxHealth = 100;
    private final int maxWaves = 50;

    private int coins;
    private Map map;
    private Array<Enemy> activeEnemies;
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
        waveManager = new WaveManager(maxWaves);
    }

    public void startNextWave(Queue<String> enemiesToSpawn) {
        waveManager.startNextWave(enemiesToSpawn);
    }

    public void update(float delta) {
        waveManager.update(delta, this, resourceManager);

        Array<Enemy> toBeRemoved = new Array<>();

        for(Enemy enemy : activeEnemies){
            enemy.update(delta);

            if (enemy.getPosition().dst(base.getPosition()) < 5.0f) {
                boolean alive = base.takeDamage(enemy.getHp());
                if (!alive) {
                    screenManager.switchScreen(ScreenType.MAIN_MENU);
                }
                toBeRemoved.add(enemy);
            }
        }

        for (Enemy enemy : toBeRemoved){
            activeEnemies.removeValue(enemy, true);
        }
    }

    public void spawnEnemy(Enemy enemy) {
        activeEnemies.add(enemy);
    }

    public void draw(SpriteBatch batch) {
        Texture background = map.getBackground();

        batch.begin();

        batch.draw(background, 0, 0);

        for(Enemy enemy : activeEnemies){
            enemy.draw(batch);
        }

        batch.end();
    }
}
