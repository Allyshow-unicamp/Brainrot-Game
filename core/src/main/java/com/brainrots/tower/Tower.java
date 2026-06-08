package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.LevelManager;
import com.brainrots.entity.GameObject;

public abstract class Tower extends GameObject {
    private String id;
    private String name;
    protected int damage;
    protected int attackSpeed;
    protected float range;
    protected float attackCooldown;
    private float timeSinceLastAttack;
    private TargetType targetType;
    private LevelManager levelManager;
    private GameResourceManager resourceManager;

    public Tower(Texture texture, Vector2 position, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, TargetType targetType, LevelManager levelManager, GameResourceManager resourceManager) {
        this.textureRegion = new TextureRegion(texture);
        this.position = position;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.targetType = targetType;
        this.timeSinceLastAttack = 0;
        this.levelManager = levelManager;
        this.resourceManager = resourceManager;
    }

    public abstract Projectile createProjectile(Texture texture, Enemy target);

    @Override
    public void update(float deltaTime) {
        timeSinceLastAttack += deltaTime;
        if (timeSinceLastAttack >= attackCooldown) {
            Enemy closestEnemy = levelManager.closestEnemy(position);

            if (closestEnemy == null) {
                return;
            }

            if (position.dst(closestEnemy.getPosition()) < range) {
                Texture texture = resourceManager.getTexture(Assets.PROJECTILE_BLACK);
                levelManager.spawnProjectile(createProjectile(texture, closestEnemy));
                timeSinceLastAttack = 0;
            }
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public float getRange() {
        return range;
    }
}
