package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.enemy.Enemy;
import com.brainrots.entity.GameObject;

public abstract class Tower extends GameObject {

    private String id;
    private String name;
    protected int damage;
    protected int attackSpeed;
    protected float range;
    protected float attackCooldown;
    private float lastAttackTime;
    private Enemy target;
    private TargetType targetType;

    public Tower(Texture texture, Vector2 position, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, TargetType targetType) {
        this.textureRegion = new TextureRegion(texture);
        this.position = position;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.targetType = targetType;
        this.lastAttackTime = 0;
        this.target = null;
    }

    public abstract Projectile createProjectile(Texture texture, Enemy target);

    @Override
    public void draw(SpriteBatch batch) {
        float x = position.x - width / 2f;
        float y = position.y;

        batch.draw(textureRegion, x, y, width, height);
    }
    
    public void setTarget(Enemy enemy){
        target = enemy;
    }

    public Enemy getTarget(){
        return target;
    }

    public void attack(float gameTime){
        lastAttackTime = gameTime;
    }

    public float getLastAttackTime(){
        return lastAttackTime;
    }

    public float getAttackCooldown(){
        return attackCooldown;
    }

    public boolean hasTarget(){
        if(target != null){
            return true;
        }

        return false;
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
