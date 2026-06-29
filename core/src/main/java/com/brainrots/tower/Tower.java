package com.brainrots.tower;

import java.util.Vector;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;
import com.brainrots.entity.GameObject;

import java.util.UUID;

public abstract class Tower extends GameObject {

    private String id;
    private String displayName;
    protected int damage;
    protected int attackSpeed;
    protected float range;
    protected float attackCooldown;
    private float lastAttackTime;
    private Enemy target;
    private TargetType targetType;
    protected int towerID;

    private int damageUpgradeLevel;
    private int rangeUpgradeLevel;
    private int cooldownUpgradeLevel;

    private static final int BASE_DAMAGE_COST = 50;
    private static final int BASE_RANGE_COST = 40;
    private static final int BASE_COOLDOWN_COST = 60;
    private static final float COST_MULTIPLIER = 2f;

    public Tower(Texture texture, Vector2 position, String displayName, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, TargetType targetType, int towerID) {
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
        this.targetType = targetType;
        this.lastAttackTime = 0;
        this.target = null;
        this.towerID = towerID;
        this.damageUpgradeLevel = 0;
        this.rangeUpgradeLevel = 0;
        this.cooldownUpgradeLevel = 0;
    }

    public abstract Projectile createProjectile(Texture texture, Enemy target);

    @Override
    public void draw(SpriteBatch batch) {
        float x = position.x - width / 2f;
        float y = position.y;

        batch.draw(textureRegion, x, y, width, height);
    }

    public void setTarget(Enemy enemy){
        target = enemy;
    }

    public Enemy getTarget(){
        return target;
    }

    public float getTowerHeight()
    {
        return this.height;
    }

    public float getTowerWidht()
    {
        return textureRegion.getRegionWidth();
    }

    public void attack(float gameTime){
        lastAttackTime = gameTime;
    }

    public float getLastAttackTime(){
        return lastAttackTime;
    }

    public float getAttackCooldown(){
        return attackCooldown;
    }

    public boolean hasTarget(){
        if(target != null){
            return true;
        }

        return false;
    }
    public int getDamage() {
        return damage;
    }

    public int getID()
    {
        return towerID;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public float getRange() {
        return range;
    }

    public void setID(int newID)
    {
        this.towerID = newID;
    }

    public void setPosition(Vector2 newPosition)
    {
        this.position = newPosition;
    }

    public String getName()
    {
        return this.displayName;
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

    public void upgradeRange() {
        this.range += 25;
        this.rangeUpgradeLevel++;
    }

    public void upgradeCooldown() {
        this.attackCooldown = Math.max(0.1f, this.attackCooldown * 0.85f);
        this.cooldownUpgradeLevel++;
    }

    public TargetType getTargetType() {
        return targetType;
    }
}
