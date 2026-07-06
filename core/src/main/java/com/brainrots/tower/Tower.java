package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;
import com.brainrots.entity.GameObject;

import java.util.UUID;

public abstract class Tower extends GameObject {

    private String id;
    private String displayName;
    private String description;
    protected int damage;
    protected int attackSpeed;
    protected float range;
    protected float attackCooldown;
    private float lastAttackTime;
    protected int towerID;
    protected int bonusDamage;

    protected int damageUpgradeLevel;
    protected int rangeUpgradeLevel;
    protected int cooldownUpgradeLevel;

    private static final int BASE_DAMAGE_COST = 50;
    private static final int BASE_RANGE_COST = 40;
    private static final int BASE_COOLDOWN_COST = 60;
    private static final float COST_MULTIPLIER = 2f;

    public Tower(Texture texture, Vector2 position, String displayName, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, int towerID) {
        this.id = UUID.randomUUID().toString();
        this.textureRegion = new TextureRegion(texture);
        this.position = position;
        this.width = width;
        this.height = height;
        this.displayName = displayName;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.lastAttackTime = 0;
        this.towerID = towerID;
        this.damageUpgradeLevel = 0;
        this.rangeUpgradeLevel = 0;
        this.cooldownUpgradeLevel = 0;
        this.bonusDamage = 0;
        this.description = "";
    }

    public abstract void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                                 CoinManager coinManager, ProjectileManager projectileManager, Texture projectileTexture);

    @Override
    public void draw(SpriteBatch batch) {
        float x = position.x - width / 2f;
        float y = position.y;
        batch.draw(textureRegion, x, y, width, height);
    }

    public void attack(float gameTime) {
        lastAttackTime = gameTime;
    }

    public float getLastAttackTime() {
        return lastAttackTime;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }

    public int getTotalDamage() {
        return damage + bonusDamage;
    }

    public void addBonusDamage(int amount) {
        this.bonusDamage += amount;
    }

    public int getDamage() {
        return damage;
    }

    public int getID() {
        return towerID;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public float getRange() {
        return range;
    }

    public void setPosition(Vector2 newPosition) {
        this.position = newPosition;
    }

    public String getName() {
        return this.displayName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTowerHeight() {
        return this.height;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getDamageUpgradeLevel() { return damageUpgradeLevel; }
    public int getRangeUpgradeLevel() { return rangeUpgradeLevel; }
    public int getCooldownUpgradeLevel() { return cooldownUpgradeLevel; }

    public int getDamageUpgradeCost() {
        return (int)(BASE_DAMAGE_COST * Math.pow(COST_MULTIPLIER, damageUpgradeLevel));
    }

    public int getRangeUpgradeCost() {
        return (int)(BASE_RANGE_COST * Math.pow(COST_MULTIPLIER, rangeUpgradeLevel));
    }

    public int getCooldownUpgradeCost() {
        return (int)(BASE_COOLDOWN_COST * Math.pow(COST_MULTIPLIER, cooldownUpgradeLevel));
    }

    public void upgradeDamage() {
        this.damage += 10;
        this.damageUpgradeLevel++;
    }

    public String getDamageUpgradeLabel() {
        return "Damage +10 [Lv." + damageUpgradeLevel + "]  $" + getDamageUpgradeCost();
    }

    public String getRangeUpgradeLabel() {
        return "Range +25 [Lv." + rangeUpgradeLevel + "]  $" + getRangeUpgradeCost();
    }

    public String getCooldownUpgradeLabel() {
        return "Speed +15% [Lv." + cooldownUpgradeLevel + "]  $" + getCooldownUpgradeCost();
    }

    public void upgradeRange() {
        this.range += 25;
        this.rangeUpgradeLevel++;
    }

    public void upgradeCooldown() {
        this.attackCooldown = Math.max(0.1f, this.attackCooldown * 0.85f);
        this.cooldownUpgradeLevel++;
    }
}
