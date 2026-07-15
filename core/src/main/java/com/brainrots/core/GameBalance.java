package com.brainrots.core;

public final class GameBalance {

    private GameBalance() {}

    // ==================== JOGADOR ====================

    public static final int BASE_MAX_HEALTH = 1000;
    public static final int STARTING_COINS = 10000;

    // ==================== ESTRELAS ====================

    public static final float STAR_THRESHOLD_0 = 0.20f;
    public static final float STAR_THRESHOLD_1 = 0.425f;
    public static final float STAR_THRESHOLD_2 = 0.675f;

    // ==================== INIMIGOS ====================

    public static final int ENEMY_RED_HP = 100;
    public static final int ENEMY_RED_SPEED = 400;
    public static final int ENEMY_RED_REWARD = 10;

    public static final int ENEMY_BLUE_HP = 300;
    public static final int ENEMY_BLUE_SPEED = 300;
    public static final int ENEMY_BLUE_REWARD = 30;

    public static final int ENEMY_GREEN_HP = 60;
    public static final int ENEMY_GREEN_SPEED = 1000;
    public static final int ENEMY_GREEN_REWARD = 10;

    public static final int ENEMY_ORANGE_HP = 1500;
    public static final int ENEMY_ORANGE_SPEED = 150;
    public static final int ENEMY_ORANGE_REWARD = 100;

    public static final int ENEMY_WHITE_HP = 5000;
    public static final int ENEMY_WHITE_SPEED = 450;
    public static final int ENEMY_WHITE_REWARD = 100;

    // ==================== TORRES (valores em jogo) ====================

    public static final int TOWER_TUNG_DAMAGE = 60;
    public static final int TOWER_TUNG_COST = 150;
    public static final float TOWER_TUNG_COOLDOWN = 0.45f;
    public static final int TOWER_TUNG_PROJ_SPEED = 2500;

    public static final int TOWER_BOMB_DAMAGE = 100;
    public static final int TOWER_BOMB_COST = 300;
    public static final float TOWER_BOMB_COOLDOWN = 0.9f;
    public static final int TOWER_BOMB_PROJ_SPEED = 2000;
    public static final float TOWER_BOMB_AOE = 150f;

    public static final int TOWER_NUC_DAMAGE = 70;
    public static final int TOWER_NUC_COST = 450;
    public static final float TOWER_NUC_COOLDOWN = 1.75f;

    public static final int TOWER_COIN_COST = 600;
    public static final float TOWER_COIN_COOLDOWN = 2.75f;
    public static final int TOWER_COIN_PER_TOWER = 7;

    public static final int TOWER_BOOST_COST = 750;
    public static final float TOWER_BOOST_COOLDOWN = 2.0f;
    public static final int TOWER_BOOST_AMOUNT = 7;

    // ==================== EFEITOS ====================

    public static final int FIRE_DAMAGE_PER_TICK = 7;
    public static final float FIRE_DURATION = 3f;

    public static final int NUKE_FIRE_DAMAGE_PER_TICK = 10;
    public static final float NUKE_FIRE_DURATION = 2f;
}
