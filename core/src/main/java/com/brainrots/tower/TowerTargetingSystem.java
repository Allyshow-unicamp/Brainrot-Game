package com.brainrots.tower;

import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;

public class TowerTargetingSystem {

    private static final int MULTIPLE_TARGET_COUNT = 3;

    public Enemy findTarget(Tower tower, Array<Enemy> activeEnemies) {
        return findClosestInRange(tower, activeEnemies);
    }

    public Array<Enemy> findTargets(Tower tower, Array<Enemy> activeEnemies) {
        TargetType targetType = tower.getTargetType();
        switch (targetType) {
            case MULTIPLE:
                return findMultipleInRange(tower, activeEnemies, MULTIPLE_TARGET_COUNT);
            case AREA:
                return findAllInRange(tower, activeEnemies);
            default:
                Array<Enemy> single = new Array<>();
                Enemy closest = findClosestInRange(tower, activeEnemies);
                if (closest != null) single.add(closest);
                return single;
        }
    }

    public void searchAll(Array<Tower> activeTowers, Array<Enemy> activeEnemies) {
        for (Tower tower : activeTowers) {
            tower.setTarget(findTarget(tower, activeEnemies));
        }
    }

    private Enemy findClosestInRange(Tower tower, Array<Enemy> activeEnemies) {
        float range = tower.getRange();
        float minDist = Float.POSITIVE_INFINITY;
        Enemy closest = null;

        for (Enemy enemy : activeEnemies) {
            float dist = enemy.getPosition().dst(tower.getPosition());
            if (dist <= range && dist < minDist) {
                minDist = dist;
                closest = enemy;
            }
        }
        return closest;
    }

    private Array<Enemy> findMultipleInRange(Tower tower, Array<Enemy> activeEnemies, int count) {
        float range = tower.getRange();
        Array<Enemy> inRange = new Array<>();

        for (Enemy enemy : activeEnemies) {
            float dist = enemy.getPosition().dst(tower.getPosition());
            if (dist <= range) {
                inRange.add(enemy);
            }
        }

        inRange.sort((a, b) -> Float.compare(
            a.getPosition().dst(tower.getPosition()),
            b.getPosition().dst(tower.getPosition())
        ));

        Array<Enemy> result = new Array<>();
        int limit = Math.min(count, inRange.size);
        for (int i = 0; i < limit; i++) {
            result.add(inRange.get(i));
        }
        return result;
    }

    private Array<Enemy> findAllInRange(Tower tower, Array<Enemy> activeEnemies) {
        float range = tower.getRange();
        Array<Enemy> inRange = new Array<>();

        for (Enemy enemy : activeEnemies) {
            float dist = enemy.getPosition().dst(tower.getPosition());
            if (dist <= range) {
                inRange.add(enemy);
            }
        }
        return inRange;
    }
}
