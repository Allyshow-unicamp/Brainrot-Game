package com.brainrots;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy {

    private float hp;
    private float speed;
    private Array<Vector2> path;
    private int currentWaypointIndex;
    private Vector2 pos;

    Enemy(Array<Vector2> path){
        this.path = path;

        pos = path.get(0);
        currentWaypointIndex = 1;
        speed = 500;
    }

    public void update(float delta){
        
        Vector2 vecDist = (new Vector2(path.get(currentWaypointIndex).x, path.get(currentWaypointIndex).y)).sub(pos);

        if(vecDist.len2() < 100){
            currentWaypointIndex++;
        }

        Vector2 direction = vecDist.nor();

        System.out.println("going to:" + direction.y + "distance: " + vecDist.len2());


        pos.x += direction.x * speed * delta;
        pos.y += direction.y * speed * delta;



    }

    public void draw(SpriteBatch batch){
        Sprite temp = new Sprite(new Texture("temp.png"));

        temp.setCenter(pos.x, pos.y);
        temp.draw(batch);

    }
}
