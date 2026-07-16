package com.brainrots.core;

/** Constantes de balanceamento centralizadas: stats de jogador, torres, inimigos, upgrades e sinergias. */
public final class GameBalance {

    private GameBalance() {}

    // ==================== VOLUME =====================

    public static final float VOLUME = 0.25f;

    // ==================== JOGADOR ====================

    public static final int BASE_MAX_HEALTH = 1000;
    public static final int STARTING_COINS = 500;
    public static final float DEFAULT_SPEED_MULTIPLIER = 1f;

    // ==================== ESTRELAS ====================

    public static final float STAR_THRESHOLD_1 = 0.33f;
    public static final float STAR_THRESHOLD_2 = 0.66f;

    // ================= MOEDAS AO VENCER ===============

    public static final int COINS_1_STAR = 50;
    public static final int COINS_2_STAR = 100;
    public static final int COINS_3_STAR = 200;

    // ==================== INIMIGOS ====================

    public static final int ENEMY_RED_HP = 200;
    public static final int ENEMY_RED_SPEED = 400;
    public static final int ENEMY_RED_REWARD = 10;

    public static final int ENEMY_BLUE_HP = 1000;
    public static final int ENEMY_BLUE_SPEED = 300;
    public static final int ENEMY_BLUE_REWARD = 50;

    public static final int ENEMY_GREEN_HP = 100;
    public static final int ENEMY_GREEN_SPEED = 1000;
    public static final int ENEMY_GREEN_REWARD = 25;

    public static final int ENEMY_ORANGE_HP = 7500;
    public static final int ENEMY_ORANGE_SPEED = 400;
    public static final int ENEMY_ORANGE_REWARD = 250;

    public static final int ENEMY_WHITE_HP = 20000;
    public static final int ENEMY_WHITE_SPEED = 450;
    public static final int ENEMY_WHITE_REWARD = 500;

    // ==================== TORRES (valores em jogo) ====================

    public static final int TOWER_TUNG_COST = 150;
    public static final int TOWER_TUNG_DAMAGE = 50;
    public static final float TOWER_TUNG_COOLDOWN = 0.45f;
    public static final int TOWER_TUNG_PROJ_SPEED = 2500;
    public static final float TOWER_TUNG_RANGE = 250f;

    public static final int TOWER_BOMB_COST = 300;
    public static final int TOWER_BOMB_DAMAGE = 100;
    public static final float TOWER_BOMB_COOLDOWN = 0.9f;
    public static final int TOWER_BOMB_PROJ_SPEED = 2000;
    public static final float TOWER_BOMB_AOE = 250f;
    public static final float TOWER_BOMB_RANGE = 350f;

    public static final int TOWER_NUC_COST = 400;
    public static final int TOWER_NUC_DAMAGE = 75;
    public static final float TOWER_NUC_COOLDOWN = 1.0f;
    public static final float TOWER_NUC_RANGE = 200f;

    public static final int TOWER_COIN_COST = 300;
    public static final float TOWER_COIN_COOLDOWN = 2.75f;
    public static final int TOWER_COIN_PER_TOWER = 10;
    public static final float TOWER_COIN_RANGE = 200f;

    public static final int TOWER_BOOST_COST = 750;
    public static final float TOWER_BOOST_COOLDOWN = 2.0f;
    public static final int TOWER_BOOST_AMOUNT = 10;
    public static final float TOWER_BOOST_RANGE = 250f;

    // ==================== TORRES (upgrades) ===========================

    public static final int UPGRADE_DAMAGE = 50;
    public static final int UPGRADE_RANGE = 50;
    public static final float UPGRADE_COOLDOWN = 0.85f;

    public static final int TOWER_AREA_RANGE_UPGRADE = 20;

    public static final int TOWER_COIN_UPGRADE_AMOUNT = 5;
    public static final int TOWER_BOOST_UPGRADE_AMOUNT = 10;

    public static final int UPGRADE_BASE_DAMAGE_COST = 50;
    public static final int UPGRADE_BASE_RANGE_COST = 25;
    public static final int UPGRADE_BASE_COOLDOWN_COST = 50;
    public static final float UPGRADE_COST_MULTIPLIER = 2f;

    // ==================== TORRES (posicionamento) =====================

    public static final float DRAG_THRESHOLD = 20f;
    public static final int TOWER_SIZE = 150;
    public static final int SAMPLE_GRID = 10;
    public static final float VALID_THRESHOLD = 0.67f;
    public static final float PATH_THRESHOLD = 10f;

    // ==================== EFEITOS ====================

    public static final int FIRE_DAMAGE_PER_TICK = 1;
    public static final float FIRE_DURATION = 0.8f;

    public static final int NUKE_FIRE_DAMAGE_PER_TICK = 10;
    public static final float NUKE_FIRE_DURATION = 2f;

    // ==================== SINERGIAS ====================

    public static final int FIRE_SYNERGY_THRESHOLD = 3;
    public static final float FIRE_SYNERGY_DAMAGE_MULT = 1.15f;

    public static final int ICE_SYNERGY_THRESHOLD = 3;
    public static final float ICE_SYNERGY_DAMAGE_MULT = 1.15f;

    public static final int PHYSICAL_SYNERGY_THRESHOLD = 3;
    public static final float PHYSICAL_SYNERGY_DAMAGE_MULT = 1.10f;

    public static final int SUPPORT_SYNERGY_THRESHOLD = 3;
    public static final float SUPPORT_SYNERGY_DAMAGE_MULT = 1.0f;
    public static final float SUPPORT_SYNERGY_RANGE_MULT = 2.0f;
    public static final float SUPPORT_SYNERGY_COOLDOWN_MULT = 1.0f;
}
