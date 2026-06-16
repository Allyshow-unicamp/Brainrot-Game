package com.brainrots.tower;

import com.brainrots.core.Assets;

public enum TestTower {
    
    TOWER_PATAPIM(Assets.TOWER_PATAPIM,25,2000,250f,0.5f,150,150,TargetType.SINGLE,0),
    TOWER_CAPPUCCINO(Assets.TOWER_CAPPUCCINO,25,2000,250f,0.5f,150,150,TargetType.SINGLE,0),
    TOWER_VACA_SATURNITA(Assets.TOWER_VACA_SATURNITA,25,2000,250f,0.5f,150,150,TargetType.SINGLE,0);


    private final String texture;
    private final int damage;
    private final int attackSpeed;
    private final float range;
    private final float attackCooldown;
    private final int width;
    private final int height;
    private final TargetType targetType;
    private final int numberUpgrades;

    TestTower(String texture, int damage, int attackSpeed, float range, float attackCooldown, 
              int width, int height, TargetType targetType, int numberUpgrades) 
    {
        this.texture = texture;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.width = width;
        this.height = height;
        this.targetType = targetType;
        this.numberUpgrades= numberUpgrades;
    }

    public String getTexture() 
    { 
        return texture; 
    }
    public int getDamage() 
    { 
        return damage; 
    }
    public int getAttackSpeed() 
    { 
        return attackSpeed; 
    }
    public float getRange() 
    { 
        return range; 
    }
    public float getAttackCooldown() 
    { 
        return attackCooldown; 
    }
    public int getWidth() 
    { 
        return width; 
    }
    public int getHeight() 
    { 
        return height; 
    }
    public TargetType getTargetType() 
    { 
        return targetType; 
    }
    public int getNumberUpgrades()
    {
        return numberUpgrades;
    }
}
