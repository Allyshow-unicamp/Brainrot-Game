package com.brainrots.effect;

import com.brainrots.enemy.Enemy;

/**
 * Classe abstrata que define um efeito de status aplicável a inimigos.
 *
 * Padrões: Strategy + Template Method + Prototype.
 *
 * Strategy: cada efeito concreto (Fire, Ice, Poison, Stun) encapsula
 * seu próprio comportamento de apply/tick/expire em métodos polimórficos,
 * eliminando qualquer switch baseado em tipo de efeito no Enemy ou Projectile.
 *
 * Template Method: o método update() define o esqueleto do processamento:
 * tick() → decrementa timer → onExpire() se acabou. Subclasses implementam
 * apenas os hooks (tick, onApply, onExpire) sem controlar o fluxo.
 *
 * Prototype: copy() permite que cada projétil ou torre crie instâncias
 * independentes do efeito, evitando que múltiplos inimigos compartilhem
 * a mesma instância e seus timers.
 */
public abstract class StatusEffect {

    protected float duration;
    protected float remainingTime;

    public StatusEffect(float duration) {
        this.duration = duration;
        this.remainingTime = duration;
    }

    /** Chamado uma vez quando o efeito é aplicado ao inimigo.
     *  Exemplo: IceEffect reduz a velocidade do inimigo imediatamente. */
    public abstract void onApply(Enemy enemy);

    /** Chamado a cada frame enquanto o efeito estiver ativo.
     *  Exemplo: FireEffect causa dano contínuo (DOT) a cada tick. */
    public abstract void tick(Enemy enemy, float delta);

    /** Chamado quando a duração do efeito expira.
     *  Exemplo: IceEffect restaura a velocidade original do inimigo. */
    public abstract void onExpire(Enemy enemy);

    /** Retorna uma cópia independente deste efeito (padrão Prototype).
     *  Necessário porque cada inimigo que recebe o efeito precisa de sua
     *  própria instância com timer independente. */
    public abstract StatusEffect copy();

    public boolean isExpired() {
        return remainingTime <= 0;
    }

    /**
     * Template Method: processa o efeito neste frame.
     *
     * A ordem é importante para garantir consistência:
     * 1. tick() — aplica o efeito neste frame (ex: causa dano DOT)
     * 2. Decrementa o timer
     * 3. onExpire() — se o efeito acabou, limpa o estado no inimigo
     *
     * Se o efeito já expirou, não faz nada. Isso permite chamar update()
     * incondicionalmente no loop do Enemy.
     */
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
