package com.brainrots.effect;

import com.brainrots.enemy.Enemy;

/**
 * Efeito de fogo: causa dano ao longo do tempo (Damage Over Time - DOT).
 *
 * A cada tick (frame), aplica damagePerTick ao inimigo. Diferente de
 * PoisonEffect, que pode ter mecânicas distintas no futuro (ex: dano
 * acumulativo ou escalonado).
 *
 * O dano é aplicado independentemente do ataque da torre — ou seja,
 * um inimigo em chamas continua tomando dano mesmo depois que a torre
 * muda de alvo.
 */
public class FireEffect extends StatusEffect {

    private int damagePerTick;

    /**
     * @param duration       duração total do efeito em segundos
     * @param damagePerTick  dano causado a cada frame (tick)
     */
    public FireEffect(float duration, int damagePerTick) {
        super(duration);
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void onApply(Enemy enemy) {
        // FireEffect não precisa de configuração inicial no inimigo.
    }

    @Override
    public void tick(Enemy enemy, float delta) {
        enemy.takeDamage(damagePerTick);
    }

    @Override
    public void onExpire(Enemy enemy) {
        // Nada a limpar — o DOT não altera estado permanente do inimigo.
    }

    @Override
    public StatusEffect copy() {
        return new FireEffect(duration, damagePerTick);
    }

    public int getDamagePerTick() {
        return damagePerTick;
    }
}
