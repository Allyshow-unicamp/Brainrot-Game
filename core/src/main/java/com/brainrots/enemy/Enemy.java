package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.entity.GameObject;

public abstract class Enemy extends GameObject {
    
    protected int hp;
    protected int MAX_HP;
    protected int speed;
    protected int rewardCoins;
    private Array<Vector2> path;
    private int currentWaypointIndex;

    public Enemy(Array<Vector2> path, Texture texture, int health, int speed, int width, int height) {
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

        currentWaypointIndex = 1;
        this.speed = speed;
    }

    public void update(float delta) {
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

    public boolean takeDamage(int damage) {
        hp -= damage;
        return hp <= 0;
    }

    public int getHp() {
        return hp;
    }

    public boolean isDead()
    {
        if(hp <= 0)
            return true;
        return false;
    }

    public int giveCoinOnDeath()
    {
        return this.rewardCoins;
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer)
    {
        float barWidth = width; 
        float barHeight = 6f;   
        
        float barX = position.x + (width / 2f) - (barWidth / 2f);
        float barY = position.y + height + 5f;

        // Fundo da barra
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        // Vida atual
        float hpPercentage = (float) hp / MAX_HP;
        float currentBarWidth = barWidth * hpPercentage;

        // Barra com vida atual
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, currentBarWidth, barHeight);
    }
}
