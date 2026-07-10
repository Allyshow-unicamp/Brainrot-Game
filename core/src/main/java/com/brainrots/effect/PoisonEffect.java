package com.brainrots.effect;

import com.brainrots.enemy.Enemy;

/**
 * Efeito de veneno: causa dano ao longo do tempo (DOT), similar ao FireEffect.
 *
 * A principal diferença conceitual é que PoisonEffect pode ter mecânicas
 * distintas no futuro, como:
 * - Dano que escala com o tempo (cada tick causa mais dano que o anterior)
 * - Acúmulo: múltiplas aplicações aumentam o dano por tick
 * - Redução de cura ou outros efeitos secundários
 *
 * Por enquanto, é um DOT simples com dano fixo por tick, mas separado
 * do FireEffect para permitir especialização futura sem modificar código
 * existente (OCP - Open/Closed Principle).
 */
public class PoisonEffect extends StatusEffect {

    private int damagePerTick;

    /**
     * @param duration       duração total em segundos
     * @param damagePerTick  dano causado a cada tick
     */
    public PoisonEffect(float duration, int damagePerTick) {
        super(duration);
        this.damagePerTick = damagePerTick;
    }

    @Override
    public void onApply(Enemy enemy) {
        // Sem estado inicial necessário.
    }

    @Override
    public void tick(Enemy enemy, float delta) {
        enemy.takeDamage(damagePerTick);
    }

    @Override
    public void onExpire(Enemy enemy) {
        // Nada a limpar.
    }

    @Override
    public StatusEffect copy() {
        return new PoisonEffect(duration, damagePerTick);
    }

    public int getDamagePerTick() {
        return damagePerTick;
    }
}
