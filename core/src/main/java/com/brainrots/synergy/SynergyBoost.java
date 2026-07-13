package com.brainrots.synergy;

/**
 * Representa um modificador numérico aplicado a uma torre por uma
 * sinergia ativa. Encapsula multiplicadores independentes para dano,
 * alcance e cooldown, permitindo que diferentes sinergias modifiquem
 * aspectos distintos da mesma torre sem conflito.
 *
 * Os multiplicadores são compostos (multiplicados entre si) no cálculo
 * final de Tower.getTotalDamage(), getRange() e getAttackCooldown().
 * Isso significa que múltiplas sinergias ativas stackam
 * multiplicativamente (ex: dois boosts de 1.15x resultam em 1.3225x).
 *
 * Uso típico:
 *   new SynergyBoost(1.15f)          // +15% dano
 *   new SynergyBoost(1.0f, 2.0f)     // dobra o alcance
 *   new SynergyBoost(1.0f, 1.0f, 0.85f) // -15% cooldown
 */
public class SynergyBoost {

    private float damageMultiplier;
    private float rangeMultiplier;
    private float cooldownMultiplier;

    /**
     * @param damageMultiplier  multiplicador de dano (1.0 = neutro, 1.15 = +15%)
     * @param rangeMultiplier   multiplicador de alcance (1.0 = neutro)
     * @param cooldownMultiplier multiplicador de cooldown (1.0 = neutro, 0.85 = -15%)
     */
    public SynergyBoost(float damageMultiplier, float rangeMultiplier, float cooldownMultiplier) {
        this.damageMultiplier = damageMultiplier;
        this.rangeMultiplier = rangeMultiplier;
        this.cooldownMultiplier = cooldownMultiplier;
    }

    /** Construtor conveniente para boost apenas de dano. */
    public SynergyBoost(float damageMultiplier) {
        this(damageMultiplier, 1.0f, 1.0f);
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getRangeMultiplier() {
        return rangeMultiplier;
    }

    public float getCooldownMultiplier() {
        return cooldownMultiplier;
    }
}
