package com.brainrots.tower;

import com.brainrots.core.Assets;

public enum TestTower {

    TOWER_TUNG(Assets.TOWER_TUNG, 25, 2000, 250f, 0.5f, 150, 150, TargetType.SINGLE, 0, 50),
    TOWER_VACA_SATURNITA(Assets.TOWER_VACA_SATURNITA, 30, 1800, 275f, 0.6f, 150, 150, TargetType.SINGLE, 0, 80),
    TOWER_TRIPPI_TROPPI(Assets.TOWER_TRIPPI_TROPPI, 35, 1600, 300f, 0.7f, 150, 150, TargetType.MULTIPLE, 0, 100),
    TOWER_TRALALERO_TRALALA(Assets.TOWER_TRALALERO_TRALALA, 20, 2200, 200f, 0.4f, 150, 150, TargetType.SINGLE, 0, 60),
    TOWER_PATAPIM(Assets.TOWER_PATAPIM, 25, 2000, 250f, 0.5f, 150, 150, TargetType.SINGLE, 1, 75),
    TOWER_NUCLEARO_DINOSAURO(Assets.TOWER_NUCLEARO_DINOSAURO, 50, 1200, 350f, 1.2f, 150, 150, TargetType.AREA, 0, 150),
    TOWER_LIRILI_LARILA(Assets.TOWER_LIRILI_LARILA, 15, 2500, 200f, 0.3f, 150, 150, TargetType.SINGLE, 0, 40),
    TOWER_CAPPUCCINO(Assets.TOWER_CAPPUCCINO, 25, 2000, 250f, 0.5f, 150, 150, TargetType.SINGLE, 1, 75),
    TOWER_CAPPUCCINA(Assets.TOWER_CAPPUCCINA, 40, 1400, 300f, 0.8f, 150, 150, TargetType.MULTIPLE, 0, 120),
    TOWER_BOMBARDILO_CROCODILO(Assets.TOWER_BOMBARDILO_CROCODILO, 45, 1000, 400f, 1.0f, 150, 150, TargetType.AREA, 0, 200);

    private final String texture;
    private final int damage;
    private final int attackSpeed;
    private final float range;
    private final float attackCooldown;
    private final int width;
    private final int height;
    private final TargetType targetType;
    private final int numberUpgrades;
    private final int cost;

    TestTower(String texture, int damage, int attackSpeed, float range, float attackCooldown,
              int width, int height, TargetType targetType, int numberUpgrades, int cost) {
        this.texture = texture;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.width = width;
        this.height = height;
        this.targetType = targetType;
        this.numberUpgrades = numberUpgrades;
        this.cost = cost;
    }

    public String getTexture() { return texture; }
    public int getDamage() { return damage; }
    public int getAttackSpeed() { return attackSpeed; }
    public float getRange() { return range; }
    public float getAttackCooldown() { return attackCooldown; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public TargetType getTargetType() { return targetType; }
    public int getNumberUpgrades() { return numberUpgrades; }
    public int getCost() { return cost; }
}
