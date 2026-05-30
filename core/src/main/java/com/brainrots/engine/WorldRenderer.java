package com.brainrots.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brainrots.enemy.Enemy;
import com.brainrots.projectile.Projectile;
import com.brainrots.tower.Tower;

public class WorldRenderer {
    public void render(SpriteBatch batch, LevelManager level) {
        Texture background = level.getMapBackground();
        batch.draw(background, 0, 0);

        for (Enemy enemy : level.getActiveEnemies()) {
            enemy.draw(batch);
        }

        for (Tower tower : level.getActiveTowers()) {
            tower.draw(batch);
        }

        for (Projectile projectile : level.getActiveProjectiles()) {
            projectile.draw(batch);
        }
    }
}
