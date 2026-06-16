package com.brainrots.tower;

import java.util.Vector;

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
    protected int numberUpgrades;
    protected Vector<Tower> avaiableUpgrades;
    protected int towerID;

    public Tower(Texture texture, Vector2 position, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, TargetType targetType, int numberUpgrades, int towerID) {
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
        this.numberUpgrades = numberUpgrades;
        this.towerID = towerID;
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

    public float getTowerHeight()
    {
        return this.height;
    }

    public float getTowerWidht()
    {
        return textureRegion.getRegionWidth();
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

    public int getID()
    {
        return towerID;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    public float getRange() {
        return range;
    }

    public void setID(int newID)
    {
        this.towerID = newID;
    }

    public void setPosition(Vector2 newPosition)
    {
        this.position = newPosition;
    }

    public boolean wasClicked(float mouseXCoord, float mouseYCoord)
    {
        //Determinar area da colisao
        float initialXCoord = position.x - width / 2f;
        float finalXCoord = initialXCoord + width;
        float initialYCoord = position.y;
        float finalYCoord = position.y + height;

        return(onXRange(initialXCoord, finalXCoord, mouseXCoord) && 
        onYRange(initialYCoord, finalYCoord, mouseYCoord));
    }

    /**
     * Metodo ocmplementar de wasClicked, determina 
     * se a coordenada do mouse foi dentro do intervalo
     */
    private boolean onXRange(float initialXCoord, float finalXCoord, float currentXCoord)
    {
        if(currentXCoord < finalXCoord && currentXCoord > initialXCoord)
            return true;
        return false;
    }

    /**
     * Metodo ocmplementar de wasClicked, determina 
     * se a coordenada do mouse foi dentro do intervalo
     */
    private boolean onYRange(float initialYCoord, float finalYCoord, float currentYCoord)
    {
        if(currentYCoord < finalYCoord && currentYCoord > initialYCoord)
            return true;
        return false;
    }  

    public int getNumberUpgrades()
    {
        return this.numberUpgrades;
    }
}
