package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;

public class TowerCoin extends Tower {

    private int coinsPerTower;

    public TowerCoin(Texture texture, Vector2 position, String displayName,
            float range, float attackCooldown, int width, int height, int towerID, int coinsPerTower) {
        super(texture, position, displayName, 0, 0, range, attackCooldown, width, height, towerID);
        this.coinsPerTower = coinsPerTower;
    }

    @Override
    public void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                        CoinManager coinManager, ProjectileManager projectileManager, Texture projectileTexture) {
        if (gameTime - getLastAttackTime() < attackCooldown) return;

        int count = 0;
        for (int i = 0; i < activeTowers.size; i++) {
            Tower other = activeTowers.get(i);
            if (other != this && other.getPosition().dst(position) <= range) {
                count++;
            }
        }
        if (count > 0) {
            coinManager.addCoins(count * coinsPerTower);
        }
        attack(gameTime);
    }

    @Override
    public void upgradeDamage() {
        this.coinsPerTower += 3;
        this.damageUpgradeLevel++;
    }

    @Override
    public String getDamageUpgradeLabel() {
        return "Coins/Tower [Lv." + damageUpgradeLevel + "]  $" + getDamageUpgradeCost();
    }
}
