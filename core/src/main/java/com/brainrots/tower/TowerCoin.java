package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.effect.StatusEffect;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;

public class TowerCoin extends Tower {

    private int coinsPerTower;

    public TowerCoin(Texture texture, Texture synergyTexture,Vector2 position, String displayName,
            float range, float attackCooldown, int width, int height, BrainrotTower towerType, int coinsPerTower) {
        super(texture,synergyTexture ,position, displayName, 0, 0, range, attackCooldown, width, height, towerType);
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

    /**
     * TowerCoin não causa dano, portanto não aplica efeitos de status.
     */
    @Override
    public StatusEffect createEffect() {
        return null;
    }

    @Override
    public void upgradeDamage() {
        this.coinsPerTower += 3;
        this.damageUpgradeLevel++;
    }

    @Override
    public String getDamageUpgradeLabel() {
        return "Coins/Tower +3 [Lv." + damageUpgradeLevel + "]  $" + getDamageUpgradeCost();
    }

    @Override
    public String getRangeUpgradeLabel() {
        return "Range +25 [Lv." + rangeUpgradeLevel + "]  $" + getRangeUpgradeCost();
    }

    @Override
    public String getCooldownUpgradeLabel() {
        return "Speed +15% [Lv." + cooldownUpgradeLevel + "]  $" + getCooldownUpgradeCost();
    }
}
