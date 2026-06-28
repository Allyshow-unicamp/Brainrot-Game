package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;

import java.util.HashMap;

public class EnemyFactory {

    private static EnemyFactory enemyFactory;
    private HashMap<String, Spawnable> mapOfEnemies;

    private EnemyFactory() {
        HashMap<String, Spawnable> newMap = new HashMap<>();
        newMap.put("67_Ball", new EnemyTemplate(Assets.ENEMY_67, 200, 200, 100, 100, 100));
        newMap.put("6_Ball", new EnemyTemplate(Assets.ENEMY_6, 100, 200, 100, 100, 100));
        newMap.put("7_Ball", new EnemyTemplate(Assets.ENEMY_7, 50, 300, 100, 100, 100));
        newMap.put("6_7_Ball", new EnemyTemplate(Assets.ENEMY_6_7, 1000, 100, 100, 100, 100));

        this.mapOfEnemies = newMap;
    }

    public static EnemyFactory getEnemyFactory() {
        if (enemyFactory == null)
            enemyFactory = new EnemyFactory();
        return enemyFactory;
    }

    public Enemy spawnEnemy(String enemy, Array<Vector2> path) {
        Spawnable prototype = this.mapOfEnemies.get(enemy);
        return prototype.spawn(path);
    }
}
