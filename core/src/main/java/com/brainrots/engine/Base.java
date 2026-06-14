package com.brainrots.engine;

import com.badlogic.gdx.math.Vector2;

public class Base {
    private int health;
    private int maxHealth;
    private Vector2 position;

    public Base(int maxHealth, Vector2 initialPosition) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.position = initialPosition;
    }

    public void takeDamage(int damage) {
        health -= Math.min(health, damage);
    }

    public boolean hasHealth(){
        if(health > 0){
            return true;
        }

        return false;
    }

    public int calculateStars() {
        float score = (float) this.health / (float) this.maxHealth * 100;
        int stars = score < 25 ? 0 : (score < 50 ? 1 : (score < 75) ? 2 : 3);
        return stars;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getHealth() {
        return health;
    }
}
