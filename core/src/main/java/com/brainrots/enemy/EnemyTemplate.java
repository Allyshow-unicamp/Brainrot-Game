package com.brainrots.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;

public class EnemyTemplate implements Spawnable {

    private String texturePath;
    private int health;
    private int speed;
    private int width;
    private int height;
    private int rewardCoins;

    public EnemyTemplate(String texturePath, int health, int speed, int width, int height, int rewardCoins) {
        this.texturePath = texturePath;
        this.health = health;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.rewardCoins = rewardCoins;
    }

    public Enemy spawn(Array<Vector2> path) {
        Texture texture = GameResourceManager.getGameResourceManager().getTexture(texturePath);
        return new Enemy(path, texture, health, speed, width, height, rewardCoins);
    }

    public int getHealth() { return health; }
    public int getSpeed() { return speed; }
    public int getRewardCoins() { return rewardCoins; }
}
