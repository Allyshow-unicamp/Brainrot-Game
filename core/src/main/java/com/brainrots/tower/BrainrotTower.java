package com.brainrots.tower;

import com.brainrots.core.Assets;
import com.brainrots.core.GameBalance;
import com.brainrots.effect.FireEffect;
import com.brainrots.effect.StatusEffect;
import com.brainrots.synergy.SynergyGroup;

public enum BrainrotTower {

    TUNG_TUNG("tung_tung", "Tung Tung Tung Sahur", Assets.TOWER_TUNG, Assets.TUNG_SYNERGY , GameBalance.TOWER_TUNG_DAMAGE, GameBalance.TOWER_TUNG_PROJ_SPEED, GameBalance.TOWER_TUNG_RANGE, GameBalance.TOWER_TUNG_COOLDOWN, 150, 150, GameBalance.TOWER_TUNG_COST, 0, 0f,
        "Fires projectiles at a single enemy. Low cost, ideal for early game.",
        SynergyGroup.PHYSICAL, null),
    BOMBARDILO_CROCODILO("bombardiro_crocodilo", "Bombardiro Crocodilo", Assets.TOWER_BOMBARDILO_CROCODILO,Assets.BOMBARDILO_SYNERGY ,GameBalance.TOWER_BOMB_DAMAGE, GameBalance.TOWER_BOMB_PROJ_SPEED, GameBalance.TOWER_BOMB_RANGE, GameBalance.TOWER_BOMB_COOLDOWN, 150, 150, GameBalance.TOWER_BOMB_COST, 0, GameBalance.TOWER_BOMB_AOE,
        "Fires explosive projectiles that damage multiple enemies in an area and applies fire damage over time.",
        SynergyGroup.FIRE, new FireEffect(GameBalance.FIRE_DURATION, GameBalance.FIRE_DAMAGE_PER_TICK)),
    NUCLEARO_DINOSAURO("nuclearo_dinosauro", "Nuclearo Dinosauro", Assets.TOWER_NUCLEARO_DINOSAURO, Assets.NUCLEARO_SYNERGY, GameBalance.TOWER_NUC_DAMAGE, 0, GameBalance.TOWER_NUC_RANGE, GameBalance.TOWER_NUC_COOLDOWN, 150, 150, GameBalance.TOWER_NUC_COST, 0, 0f,
        "Deals instant damage to all enemies within range and applies fire damage over time.",
        SynergyGroup.FIRE, new FireEffect(GameBalance.NUKE_FIRE_DURATION, GameBalance.NUKE_FIRE_DAMAGE_PER_TICK)),
    CAPPUCCINO_ASSASSINO("cappuccino_assassino", "Cappuccino Assassino", Assets.TOWER_CAPPUCCINO, Assets.CAPPUCCINO_SYNERGY, 0, 0, GameBalance.TOWER_COIN_RANGE, GameBalance.TOWER_COIN_COOLDOWN, 150, 150, GameBalance.TOWER_COIN_COST, GameBalance.TOWER_COIN_PER_TOWER, 0f,
        "Generates coins for each nearby allied tower. Does not deal damage.",
        SynergyGroup.SUPPORT, null),
    BALLERINA_CAPPUCCINA("ballerina_cappuccina", "Ballerina Cappuccina", Assets.TOWER_CAPPUCCINA, Assets.TOWER_BALLERINA, 0, 0, GameBalance.TOWER_BOOST_RANGE, GameBalance.TOWER_BOOST_COOLDOWN, 150, 150, GameBalance.TOWER_BOOST_COST, GameBalance.TOWER_BOOST_AMOUNT, 0f,
        "Boosts the damage of nearby allied towers. Does not deal damage.",
        SynergyGroup.SUPPORT, null);

    private final String towerId;
    private final String displayName;
    private final String texture;
    private final String synergyTexture;
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
    private final SynergyGroup synergyGroup;
    private final StatusEffect effectPrototype;

    BrainrotTower(String towerId, String displayName, String texture,String synergyTexture ,int damage, int attackSpeed, float range, float attackCooldown,
                  int width, int height, int cost, int extraValue, float extraFloat, String description,
                  SynergyGroup synergyGroup, StatusEffect effectPrototype) {
        this.towerId = towerId;
        this.displayName = displayName;
        this.texture = texture;
        this.synergyTexture = synergyTexture;
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
        this.synergyGroup = synergyGroup;
        this.effectPrototype = effectPrototype;
    }

    /**
     * Factory Method: cria uma cópia do efeito de status associado a esta torre.
     * Usa o padrão Prototype via copy(). Retorna null se a torre não aplica efeitos.
     */
    public StatusEffect createEffectPrototype() {
        return effectPrototype != null ? effectPrototype.copy() : null;
    }

    public String getTowerId() { return towerId; }
    public String getDisplayName() { return displayName; }
    public String getTexture() { return texture; }
    public String getSynergyTexture() {return synergyTexture;}
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
    public SynergyGroup getSynergyGroup() { return synergyGroup; }
}
