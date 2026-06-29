package com.brainrots.tower;

import com.brainrots.core.Assets;

public enum BrainrotTower {

    TUNG_TUNG("Tung Tung Tung Sahur", Assets.TOWER_TUNG, 25, 2000, 250f, 0.5f, 150, 150, 50, 0, 0f),
    BOMBARDILO_CROCODILO("Bombardiro Crocodilo", Assets.TOWER_BOMBARDILO_CROCODILO, 45, 1000, 350f, 1.0f, 150, 150, 150, 0, 150f),
    NUCLEARO_DINOSAURO("Nuclearo Dinosauro", Assets.TOWER_NUCLEARO_DINOSAURO, 30, 0, 200f, 1.5f, 150, 150, 120, 0, 0f),
    CAPPUCCINO_ASSASSINO("Cappuccino Assassino", Assets.TOWER_CAPPUCCINO, 0, 0, 200f, 3.0f, 150, 150, 80, 5, 0f),
    BALLERINA_CAPPUCCINA("Ballerina Cappuccina", Assets.TOWER_CAPPUCCINA, 0, 0, 250f, 2.0f, 150, 150, 100, 5, 0f);

    private final String displayName;
    private final String texture;
    private final int damage;
    private final int attackSpeed;
    private final float range;
    private final float attackCooldown;
    private final int width;
    private final int height;
    private final int cost;
    private final int extraValue;
    private final float extraFloat;

    BrainrotTower(String displayName, String texture, int damage, int attackSpeed, float range, float attackCooldown,
                  int width, int height, int cost, int extraValue, float extraFloat) {
        this.displayName = displayName;
        this.texture = texture;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.width = width;
        this.height = height;
        this.cost = cost;
        this.extraValue = extraValue;
        this.extraFloat = extraFloat;
    }

    public String getDisplayName() { return displayName; }
    public String getTexture() { return texture; }
    public int getDamage() { return damage; }
    public int getAttackSpeed() { return attackSpeed; }
    public float getRange() { return range; }
    public float getAttackCooldown() { return attackCooldown; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getCost() { return cost; }
    public int getExtraValue() { return extraValue; }
    public float getExtraFloat() { return extraFloat; }
}
