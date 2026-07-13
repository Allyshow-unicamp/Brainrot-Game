package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.effect.StatusEffect;
import com.brainrots.enemy.Enemy;
import com.brainrots.entity.GameObject;

/**
 * Representa um projétil disparado por uma torre em direção a um inimigo.
 *
 * Agora carrega opcionalmente um StatusEffect (Prototype) que é aplicado
 * ao inimigo ao atingi-lo, além do dano. O efeito é clonado via copy()
 * para que cada inimigo tenha sua própria instância independente.
 */
public class Projectile extends GameObject {
    private Vector2 targetPosition;
    private Enemy target;
    private boolean reachedTarget = false;
    private Vector2 mathVector = new Vector2();
    private Tower parent;
    private float aoeRadius;
    private StatusEffect effectPrototype;

    public static final float PROJECTILE_HIT_DISTANCE = 10f;

    public Projectile(Texture texture, Enemy target, Tower parent, int width, int height) {
        this(texture, target, parent, width, height, 0f, null);
    }

    public Projectile(Texture texture, Enemy target, Tower parent, int width, int height, float aoeRadius) {
        this(texture, target, parent, width, height, aoeRadius, null);
    }

    /**
     * Construtor completo que aceita um efeito de status.
     * O efeito é armazenado como protótipo e clonado no momento do impacto.
     */
    public Projectile(Texture texture, Enemy target, Tower parent, int width, int height,
                      float aoeRadius, StatusEffect effectPrototype) {
        this.textureRegion = new TextureRegion(texture);
        this.position = new Vector2(parent.getPosition());
        this.width = width;
        this.height = height;
        this.target = target;
        this.parent = parent;
        this.targetPosition = new Vector2(target.getBounds().x + target.getBounds().width / 2f,
                target.getBounds().y + target.getBounds().height / 2f);
        this.aoeRadius = aoeRadius;
        this.effectPrototype = effectPrototype;
    }

    public void update(float deltaTime) {
    if (reachedTarget || target == null) return;

    targetPosition.set(target.getPosition());
    mathVector.set(targetPosition).sub(position);
    float distanceToTarget = mathVector.len();
    
    float movementThisFrame = parent.getAttackSpeed() * deltaTime;

    if (movementThisFrame >= distanceToTarget || distanceToTarget < PROJECTILE_HIT_DISTANCE) {
        position.set(targetPosition);
        target.takeDamage(parent.getTotalDamage());
        if (effectPrototype != null) {
            target.addEffect(effectPrototype.copy());
        }
        reachedTarget = true;
    } else {
        mathVector.nor();
        position.add(mathVector.scl(movementThisFrame));
    }
}

    public boolean reachedTarget() {
        return reachedTarget;
    }

    public Enemy getTarget() {
        return target;
    }

    public Tower getParent() {
        return parent;
    }

    public boolean isAreaOfEffect() {
        return aoeRadius > 0f;
    }

    public float getAoeRadius() {
        return aoeRadius;
    }

    /** Protótipo do efeito de status que este projétil aplica ao atingir. */
    public StatusEffect getEffectPrototype() {
        return effectPrototype;
    }
}
