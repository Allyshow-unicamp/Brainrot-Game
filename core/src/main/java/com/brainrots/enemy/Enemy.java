package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.effect.StatusEffect;
import com.brainrots.entity.GameObject;

/** Inimigo que percorre waypoints e gerencia efeitos de status via composição. */
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
        if(path != null && path.size > 0)
            this.position = new Vector2(path.get(0));
        else
            this.position = new Vector2(0,0); //Pra criacao do prototipo
        this.textureRegion = texture != null ? new TextureRegion(texture) : null;
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

    /* Processa efeitos antes do movimento para que stun trave no mesmo frame */
    public void update(float delta) {
        processEffects(delta);
        if (!stunned) {
            move(delta);
        }
    }

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

    /* Itera ao contrário para remoção segura durante iteração */
    private void processEffects(float delta) {
        for (int i = activeEffects.size - 1; i >= 0; i--) {
            StatusEffect effect = activeEffects.get(i);
            effect.update(this, delta);
            if (effect.isExpired()) {
                activeEffects.removeIndex(i);
            }
        }
    }

    public void addEffect(StatusEffect effect) {
        effect.onApply(this);
        activeEffects.add(effect);
    }

    public void setStunned(boolean stunned) {
        this.stunned = stunned;
    }

    /* Usa slowStack para garantir que restore só ocorra quando o último efeito expirar */
    public void applySlow(float factor) {
        if (slowStack == 0) {
            this.speed = Math.max(1, Math.round(this.speed * factor));
        }
        slowStack++;
    }

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

    public int getSpeed() {
        return speed;
    }

    public int getOriginalSpeed() {
        return originalSpeed;
    }

    public boolean isStunned() {
        return stunned;
    }

    public Array<StatusEffect> getActiveEffects() {
        return activeEffects;
    }
}
