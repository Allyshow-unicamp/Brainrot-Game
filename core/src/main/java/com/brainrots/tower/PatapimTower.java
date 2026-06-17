package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.brainrots.core.GameResourceManager;
import com.brainrots.enemy.Enemy;


public class PatapimTower extends Tower implements TempInterfaceTower{
    
    public PatapimTower(Vector2 position ,int towerID)
    {
        super(
        GameResourceManager.getGameResourceManager().getTexture(TestTower.TOWER_PATAPIM.getTexture()), 
        position,
        TestTower.TOWER_PATAPIM.getDamage(),
        TestTower.TOWER_PATAPIM.getAttackSpeed(),
        TestTower.TOWER_PATAPIM.getRange(),
        TestTower.TOWER_PATAPIM.getAttackCooldown(),
        TestTower.TOWER_PATAPIM.getWidth(),
        TestTower.TOWER_PATAPIM.getHeight(),
        TestTower.TOWER_PATAPIM.getTargetType(),
        TestTower.TOWER_PATAPIM.getNumberUpgrades(),
        towerID
    );
    }

     @Override
    public Projectile createProjectile(Texture texture, Enemy target) {
        return new Projectile(texture, target, this, 75, 75);
    }

    protected void defineUpgradableTower(){}

    public Tower createTower(Vector2 position, int ID)
    {
        return new PatapimTower(position, ID);
    }

    public Texture getTexture()
    {
        return this.textureRegion.getTexture();
    }
}
