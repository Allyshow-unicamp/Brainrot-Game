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
        newMap.put("67_Ball", new EnemyTemplate(Assets.ENEMY_67, 175, 190, 100, 100, 125));
        newMap.put("6_Ball", new EnemyTemplate(Assets.ENEMY_6, 90, 190, 100, 100, 125));
        newMap.put("7_Ball", new EnemyTemplate(Assets.ENEMY_7, 45, 280, 100, 100, 125));
        newMap.put("6_7_Ball", new EnemyTemplate(Assets.ENEMY_6_7, 850, 95, 100, 100, 300));

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
