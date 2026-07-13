package com.brainrots.core;

public final class GameBalance {

    private GameBalance() {}

    // ==================== JOGADOR ====================

    public static final int BASE_MAX_HEALTH = 1250;
    public static final int STARTING_COINS = 400;

    // ==================== ESTRELAS ====================

    public static final float STAR_THRESHOLD_0 = 0.20f;
    public static final float STAR_THRESHOLD_1 = 0.425f;
    public static final float STAR_THRESHOLD_2 = 0.675f;

    // ==================== INIMIGOS ====================

    public static final int ENEMY_67_HP = 175;
    public static final int ENEMY_67_SPEED = 190;
    public static final int ENEMY_67_REWARD = 125;

    public static final int ENEMY_6_HP = 90;
    public static final int ENEMY_6_SPEED = 190;
    public static final int ENEMY_6_REWARD = 125;

    public static final int ENEMY_7_HP = 45;
    public static final int ENEMY_7_SPEED = 280;
    public static final int ENEMY_7_REWARD = 125;

    public static final int ENEMY_6_7_HP = 850;
    public static final int ENEMY_6_7_SPEED = 95;
    public static final int ENEMY_6_7_REWARD = 300;

    // ==================== TORRES (valores em jogo) ====================

    public static final int TOWER_TUNG_DAMAGE = 30;
    public static final int TOWER_TUNG_COST = 50;
    public static final float TOWER_TUNG_COOLDOWN = 0.45f;
    public static final int TOWER_TUNG_PROJ_SPEED = 2500;

    public static final int TOWER_BOMB_DAMAGE = 50;
    public static final int TOWER_BOMB_COST = 100;
    public static final float TOWER_BOMB_COOLDOWN = 0.9f;
    public static final int TOWER_BOMB_PROJ_SPEED = 1500;
    public static final float TOWER_BOMB_AOE = 150f;

    public static final int TOWER_NUC_DAMAGE = 35;
    public static final int TOWER_NUC_COST = 100;
    public static final float TOWER_NUC_COOLDOWN = 1.35f;

    public static final int TOWER_COIN_COST = 50;
    public static final float TOWER_COIN_COOLDOWN = 2.75f;
    public static final int TOWER_COIN_PER_TOWER = 7;

    public static final int TOWER_BOOST_COST = 100;
    public static final float TOWER_BOOST_COOLDOWN = 2.0f;
    public static final int TOWER_BOOST_AMOUNT = 7;

    // ==================== EFEITOS ====================

    public static final int FIRE_DAMAGE_PER_TICK = 7;
    public static final float FIRE_DURATION = 3f;

    public static final int NUKE_FIRE_DAMAGE_PER_TICK = 10;
    public static final float NUKE_FIRE_DURATION = 2f;

    // ==================== LOJA GLOBAL ====================

    public static final int SHOP_TUNG_PRICE = 0;
    public static final int SHOP_BOMB_PRICE = 150;
    public static final int SHOP_NUC_PRICE = 100;
    public static final int SHOP_COIN_PRICE = 100;
    public static final int SHOP_BOOST_PRICE = 100;
}
