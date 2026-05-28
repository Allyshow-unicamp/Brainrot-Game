package com.brainrots.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.ConcreteEnemy;
import com.brainrots.enemy.Enemy;

public class LevelManager {

    private final int maxHealth = 100;

    private int coins;
    private Map map;
    private Array<Enemy> activeEnemies;
    private Base base;

    public LevelManager(GameResourceManager resourceManager, String mapName){
        map = new Map(mapName, resourceManager);
        base = new Base(maxHealth, map.getLastPosition());

        activeEnemies = new Array<>();

        activeEnemies.add(new ConcreteEnemy(map.getPath(), resourceManager.getTexture("temp.png")));
    }

    public void update(float delta) {
        for(Enemy enemy : activeEnemies){
            enemy.update(delta);

            if (enemy.getPosition() == base.getPosition())
                base.takeDamage(enemy.getHp());
        }
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
