package com.brainrots.effect;

import com.brainrots.enemy.Enemy;

/** Efeito de status abstrato para inimigos. Template Method: update() orquestra tick → decremento → onExpire. */
public abstract class StatusEffect {

    protected float duration;
    protected float remainingTime;

    public StatusEffect(float duration) {
        this.duration = duration;
        this.remainingTime = duration;
    }

    public abstract void onApply(Enemy enemy);
    public abstract void tick(Enemy enemy, float delta);
    public abstract void onExpire(Enemy enemy);
    public abstract StatusEffect copy();

    public boolean isExpired() {
        return remainingTime <= 0;
    }

    /* Template Method: tick → decrementa timer → onExpire se expirou */
    public void update(Enemy enemy, float delta) {
        if (isExpired()) {
            return;
        }
        tick(enemy, delta);
        remainingTime -= delta;
        if (isExpired()) {
            onExpire(enemy);
        }
    }

    public float getDuration() {
        return duration;
    }

    public float getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(float remainingTime) {
        this.remainingTime = remainingTime;
    }
}
