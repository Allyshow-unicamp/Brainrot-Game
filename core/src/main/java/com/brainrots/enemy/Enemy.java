package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.effect.StatusEffect;
import com.brainrots.entity.GameObject;

/**
 * Representa um inimigo que percorre um caminho de waypoints.
 *
 * Gerencia efeitos de status (queimadura, lentidão, veneno, atordoamento)
 * através de composição com StatusEffect. O método update() processa
 * os efeitos ANTES do movimento, garantindo que efeitos como stun
 * impeçam o deslocamento no mesmo frame em que são aplicados.
 */
public class Enemy extends GameObject {

    protected int hp;
    protected int MAX_HP;
    protected int speed;
    protected int originalSpeed;
    protected int rewardCoins;
    private boolean stunned;
    private int slowStack;
    private Array<StatusEffect> activeEffects;
    private Array<Vector2> path;
    private int currentWaypointIndex;

    public Enemy(Array<Vector2> path, Texture texture, int health, int speed, int width, int height, int rewardCoins) {
        this.path = path;
        if(path != null)
            this.position = new Vector2(path.get(0));
        else
            this.position = new Vector2(0,0); //Pra criacao do prototipo
        this.textureRegion = new TextureRegion(texture);
        this.MAX_HP = health;
        this.hp = health;
        this.width = width;
        this.height = height;
        this.rewardCoins = rewardCoins;
        currentWaypointIndex = 1;
        this.speed = speed;
        this.originalSpeed = speed;
        this.stunned = false;
        this.slowStack = 0;
        this.activeEffects = new Array<>();
    }

    /**
     * Atualiza posição e processa efeitos de status.
     *
     * A ordem é importante:
     * 1. processEffects() — aplica ticks de DOT, verifica expirações
     * 2. Se não está atordoado, move-se ao longo do path
     *
     * Isso garante que um inimigo atordoado pare imediatamente,
     * e que um inimigo em chamas tome dano mesmo sem se mover.
     */
    public void update(float delta) {
        processEffects(delta);
        if (!stunned) {
            move(delta);
        }
    }

    /**
     * Move o inimigo ao longo do caminho de waypoints.
     * Extraído para método separado para clareza e para permitir
     * que o stun pule este bloco.
     */
    private void move(float delta) {
        if (currentWaypointIndex < path.size) {
            Vector2 target = path.get(currentWaypointIndex);
            float moveStep = speed * delta;
            float distanceToTarget = position.dst(target);

            if (distanceToTarget <= moveStep) {
                position.set(target);
                currentWaypointIndex++;
            } else {
                Vector2 direction = new Vector2(target).sub(position).nor();
                position.add(direction.scl(moveStep));
            }
        }
    }

    /**
     * Processa todos os efeitos de status ativos.
     *
     * Itera de trás para frente para permitir remoção segura
     * durante a iteração. Cada efeito tem seu update() chamado,
     * que internamente faz tick(), decrementa timer e chama
     * onExpire() se necessário.
     */
    private void processEffects(float delta) {
        for (int i = activeEffects.size - 1; i >= 0; i--) {
            StatusEffect effect = activeEffects.get(i);
            effect.update(this, delta);
            if (effect.isExpired()) {
                activeEffects.removeIndex(i);
            }
        }
    }

    /**
     * Aplica um novo efeito de status ao inimigo.
     * O efeito é adicionado à lista e seu onApply() é chamado
     * imediatamente (ex: IceEffect reduz speed, StunEffect trava).
     *
     * @param effect efeito a ser aplicado (cópia independente)
     */
    public void addEffect(StatusEffect effect) {
        effect.onApply(this);
        activeEffects.add(effect);
    }

    public void setStunned(boolean stunned) {
        this.stunned = stunned;
    }

    /**
     * Aplica um multiplicador de slow à velocidade.
     * Se já está sob efeito de slow, incrementa slowStack para
     * garantir que restoreSpeed() só restaure quando o último
     * IceEffect expirar.
     */
    public void applySlow(float factor) {
        if (slowStack == 0) {
            this.speed = Math.max(1, Math.round(this.speed * factor));
        }
        slowStack++;
    }

    /**
     * Restaura a velocidade original. Decrementa slowStack e só
     * restaura a velocidade quando a pilha chega a zero (ou seja,
     * quando todos os IceEffects expiraram).
     */
    public void restoreSpeed() {
        slowStack = Math.max(0, slowStack - 1);
        if (slowStack == 0) {
            this.speed = originalSpeed;
        }
    }

    public boolean takeDamage(int damage) {
        hp -= damage;
        return hp <= 0;
    }

    public int getHp() {
        return hp;
    }

    public boolean isDead() {
        return hp <= 0;
    }

    public int giveCoinOnDeath() {
        return this.rewardCoins;
    }

    /**
     * Verifica se o inimigo ainda está em estado de receber dano
     * ou efeitos. Útil para o LevelManager evitar processar inimigos
     * já mortos no mesmo frame.
     */
    public boolean isAlive() {
        return hp > 0;
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        float barWidth = width;
        float barHeight = 6f;

        float barX = position.x - barWidth / 2f;
        float barY = position.y + height / 2f + 5f;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        float hpPercentage = (float) hp / MAX_HP;
        float currentBarWidth = barWidth * hpPercentage;

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, currentBarWidth, barHeight);
    }

    public Array<StatusEffect> getActiveEffects() {
        return activeEffects;
    }
}
