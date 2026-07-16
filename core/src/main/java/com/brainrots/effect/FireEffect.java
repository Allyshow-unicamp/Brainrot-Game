package com.brainrots.effect;

import com.brainrots.enemy.Enemy;

/** Dano ao longo do tempo (DOT). Aplica dano por tick independentemente do ataque da torre. */
public class FireEffect extends StatusEffect {

    private int damagePerTick;

    public FireEffect(float duration, int damagePerTick) {
        super(duration);
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void onApply(Enemy enemy) {}

    @Override
    public void tick(Enemy enemy, float delta) {
        enemy.takeDamage(damagePerTick);
    }

    @Override
    public void onExpire(Enemy enemy) {}

    @Override
    public StatusEffect copy() {
        return new FireEffect(duration, damagePerTick);
    }

    public int getDamagePerTick() {
        return damagePerTick;
    }
}
