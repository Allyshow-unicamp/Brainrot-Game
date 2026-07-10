package com.brainrots.effect;

import com.brainrots.enemy.Enemy;

/**
 * Efeito de gelo: reduz a velocidade do inimigo enquanto estiver ativo.
 *
 * O slow é aplicado uma única vez em onApply() e restaurado em onExpire().
 * O controle de múltiplos slows simultâneos é feito via contagem em Enemy
 * (slowStack), garantindo que a velocidade só seja restaurada quando o
 * último efeito de gelo expirar.
 *
 * Isso evita que aplicar dois IceEffects no mesmo inimigo resulte em
 * restauração prematura da velocidade quando o primeiro expirar.
 */
public class IceEffect extends StatusEffect {

    private static final float SLOW_FACTOR = 0.5f;

    /**
     * @param duration duração do slow em segundos
     */
    public IceEffect(float duration) {
        super(duration);
    }

    /**
     * Aplica o slow multiplicando a velocidade do inimigo por SLOW_FACTOR.
     * A velocidade original é preservada em Enemy.originalSpeed.
     */
    @Override
    public void onApply(Enemy enemy) {
        enemy.applySlow(SLOW_FACTOR);
    }

    @Override
    public void tick(Enemy enemy, float delta) {
        // Slow é aplicado uma vez em onApply e mantido enquanto ativo.
    }

    /**
     * Restaura a velocidade original do inimigo.
     * Enemy.restoreSpeed() decrementa slowStack internamente e só
     * restaura a velocidade quando a pilha chega a zero.
     */
    @Override
    public void onExpire(Enemy enemy) {
        enemy.restoreSpeed();
    }

    @Override
    public StatusEffect copy() {
        return new IceEffect(duration);
    }
}
