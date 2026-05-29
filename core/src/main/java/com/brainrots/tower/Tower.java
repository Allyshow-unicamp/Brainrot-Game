package com.brainrots.tower;

import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;
import com.brainrots.projectile.Projectile;

public abstract class Tower {
    private String id;
    private String name;
    protected int damage;
    protected float range;
    protected float attackCooldown;
    protected Vector2 position;
    private int timeSinceLastAttack;
    private TargetType targetType;

    public Tower(Vector2 position, int damage, float range, float attackCooldown, TargetType targetType) {
        this.position = position;
        this.damage = damage;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.targetType = targetType;
        this.timeSinceLastAttack = 0;
    }

    public abstract Projectile createProjectile(Enemy target);

    public void update(int deltaTime) {
        timeSinceLastAttack += deltaTime;
        if (timeSinceLastAttack >= attackCooldown) {
            createProjectile(null);
            timeSinceLastAttack = 0;
        }
    }
}