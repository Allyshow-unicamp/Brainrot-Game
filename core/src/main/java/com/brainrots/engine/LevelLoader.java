package com.brainrots.engine;

import com.badlogic.gdx.math.Vector2;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.tower.ConcreteTower;
import com.brainrots.tower.TargetType;

public final class LevelLoader {
    public static void loadDefaultLevel(LevelManager level, GameResourceManager resources) {
        level.startNextWave(10, Assets.ENEMY_TEMP);

        ConcreteTower starterTower = new ConcreteTower(
            resources.getTexture(Assets.TOWER_TUNG),
            new Vector2(500, 500),
            25,
            2000,
            250,
            0.5f,
            150,
            150,
            TargetType.SINGLE,
            level,
            resources);
        level.buyAndPlaceTower(starterTower);
    }
}
