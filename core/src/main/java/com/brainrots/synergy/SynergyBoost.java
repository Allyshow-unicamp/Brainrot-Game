package com.brainrots.synergy;

/**
 * Modificador numérico aplicado a uma torre por sinergia.
 * Multiplicadores para dano, alcance e cooldown (compostos multiplicativamente).
 */
public class SynergyBoost {

    private float damageMultiplier;
    private float rangeMultiplier;
    private float cooldownMultiplier;

    /**
     * @param damageMultiplier   multiplicador de dano (1.0 = neutro)
     * @param rangeMultiplier    multiplicador de alcance (1.0 = neutro)
     * @param cooldownMultiplier multiplicador de cooldown (1.0 = neutro)
     */
    public SynergyBoost(float damageMultiplier, float rangeMultiplier, float cooldownMultiplier) {
        this.damageMultiplier = damageMultiplier;
        this.rangeMultiplier = rangeMultiplier;
        this.cooldownMultiplier = cooldownMultiplier;
    }

    /** Boost apenas de dano. */
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
