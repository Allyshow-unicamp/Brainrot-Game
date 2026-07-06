package com.brainrots.tower;

import com.brainrots.core.Assets;

public enum BrainrotTower {

    TUNG_TUNG("tung_tung", "Tung Tung Tung Sahur", Assets.TOWER_TUNG, 25, 2000, 250f, 0.5f, 150, 150, 50, 0, 0f,
        "Fires projectiles at a single enemy. Low cost, ideal for early game."),
    BOMBARDILO_CROCODILO("bombardiro_crocodilo", "Bombardiro Crocodilo", Assets.TOWER_BOMBARDILO_CROCODILO, 45, 1000, 350f, 1.0f, 150, 150, 150, 0, 150f,
        "Fires explosive projectiles that damage multiple enemies in an area."),
    NUCLEARO_DINOSAURO("nuclearo_dinosauro", "Nuclearo Dinosauro", Assets.TOWER_NUCLEARO_DINOSAURO, 30, 0, 200f, 1.5f, 150, 150, 120, 0, 0f,
        "Deals instant damage to all enemies within range on each attack."),
    CAPPUCCINO_ASSASSINO("cappuccino_assassino", "Cappuccino Assassino", Assets.TOWER_CAPPUCCINO, 0, 0, 200f, 3.0f, 150, 150, 80, 5, 0f,
        "Generates coins for each nearby allied tower. Does not deal damage."),
    BALLERINA_CAPPUCCINA("ballerina_cappuccina", "Ballerina Cappuccina", Assets.TOWER_CAPPUCCINA, 0, 0, 250f, 2.0f, 150, 150, 100, 5, 0f,
        "Boosts the damage of nearby allied towers. Does not deal damage.");

    private final String towerId;
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
    private final String description;

    BrainrotTower(String towerId, String displayName, String texture, int damage, int attackSpeed, float range, float attackCooldown,
                  int width, int height, int cost, int extraValue, float extraFloat, String description) {
        this.towerId = towerId;
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
        this.description = description;
    }

    public String getTowerId() { return towerId; }
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
    public String getDescription() { return description; }
}
