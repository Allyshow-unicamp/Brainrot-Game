package com.brainrots.engine;

import com.badlogic.gdx.math.Vector2;
import com.brainrots.core.GameBalance;

/** Base do jogador. Recebe dano quando inimigos chegam ao fim do caminho. Calcula estrelas pelo HP restante. */
public class Base {
    private int health;
    private int maxHealth;
    private Vector2 position;

    public Base(Vector2 initialPosition) {
        this(GameBalance.BASE_MAX_HEALTH, initialPosition);
    }

    public Base(int maxHealth, Vector2 initialPosition) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.position = initialPosition;
    }

    public void takeDamage(int damage) {
        health -= Math.min(health, damage);
    }

    public boolean hasHealth() {
        return health > 0;
    }

    public int calculateStars() {
        float ratio = (float) health / (float) maxHealth;
        if (ratio < GameBalance.STAR_THRESHOLD_1) return 1;
        if (ratio < GameBalance.STAR_THRESHOLD_2) return 2;
        return 3;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
