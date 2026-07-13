package com.brainrots.effect;

import com.brainrots.enemy.Enemy;

/**
 * Efeito de atordoamento: impede o inimigo de se mover enquanto estiver ativo.
 *
 * O controle é feito via flag stunned em Enemy. Quando atordoado, o inimigo
 * pula a etapa de movimento em seu update(). A flag é setada em onApply()
 * e limpa em onExpire().
 *
 * Importante: StunEffect não causa dano. É puramente um efeito de
 * controle de grupo (CC). O inimigo continua podendo receber dano
 * e efeitos enquanto atordoado.
 */
public class StunEffect extends StatusEffect {

    /**
     * @param duration duração do atordoamento em segundos
     */
    public StunEffect(float duration) {
        super(duration);
    }

    /**
     * Atordoa o inimigo imediatamente ao aplicar.
     * Enemy.setStunned(true) impede o movimento no update().
     */
    @Override
    public void onApply(Enemy enemy) {
        enemy.setStunned(true);
    }

    @Override
    public void tick(Enemy enemy, float delta) {
        // Stun não tem efeito contínuo além de impedir movimento.
    }

    /**
     * Remove o atordoamento quando o efeito expira.
     * Enemy.setStunned(false) permite que o inimigo se mova novamente.
     */
    @Override
    public void onExpire(Enemy enemy) {
        enemy.setStunned(false);
    }

    @Override
    public StatusEffect copy() {
        return new StunEffect(duration);
    }
}
