package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;
import com.brainrots.core.GameBalance;

import java.util.HashMap;

public class EnemyFactory {

    private static EnemyFactory enemyFactory;
    private HashMap<String, Spawnable> mapOfEnemies;

    private EnemyFactory() {
        HashMap<String, Spawnable> newMap = new HashMap<>();
        newMap.put("red", new EnemyTemplate(Assets.ENEMY_RED, GameBalance.ENEMY_RED_HP, GameBalance.ENEMY_RED_SPEED, 100, 100, GameBalance.ENEMY_RED_REWARD));
        newMap.put("blue", new EnemyTemplate(Assets.ENEMY_BLUE, GameBalance.ENEMY_BLUE_HP, GameBalance.ENEMY_BLUE_SPEED, 100, 100, GameBalance.ENEMY_BLUE_REWARD));
        newMap.put("orange", new EnemyTemplate(Assets.ENEMY_ORANGE, GameBalance.ENEMY_ORANGE_HP, GameBalance.ENEMY_ORANGE_SPEED, 100, 100, GameBalance.ENEMY_ORANGE_REWARD));
        newMap.put("white", new EnemyTemplate(Assets.ENEMY_WHITE, GameBalance.ENEMY_WHITE_HP, GameBalance.ENEMY_WHITE_SPEED, 100, 100, GameBalance.ENEMY_WHITE_REWARD));
        newMap.put("green", new EnemyTemplate(Assets.ENEMY_GREEN, GameBalance.ENEMY_GREEN_HP, GameBalance.ENEMY_GREEN_SPEED, 100, 100, GameBalance.ENEMY_GREEN_REWARD));


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
