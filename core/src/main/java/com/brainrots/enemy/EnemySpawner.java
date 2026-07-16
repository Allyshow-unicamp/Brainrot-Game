package com.brainrots.enemy;

/** Timer de spawn. Controla delay inicial e intervalo entre spawns de um grupo de inimigos. */
public final class EnemySpawner {

    private String type;
    private int count;
    private float spawnInterval;
    private int spawnTime;
    private boolean spawning;
    private float timer;

    public EnemySpawner(String enemy, int count, float spawnInterval, int spawnTime){
        this.type = enemy;
        this.count = count;
        this.spawnInterval = spawnInterval;
        this.spawnTime = spawnTime;
        spawning = false;
        timer = 0;
    }

    public int getCount(){
        return count;
    }

    public String getType(){
        return type;
    }

    public boolean isEmpty(){
        if(count == 0){
            return true;
        }

        return false;
    }
    
    /** Aguarda spawnTime inicial, depois spawna a cada spawnInterval. */
    public boolean shouldSpawn(float delta){
        timer += delta;

        if(spawning == false){
            if(timer >= spawnTime){
                spawning = true;
            }
            else{
                return false;
            }
        }

        if(timer >= spawnInterval){
            count --;
            timer = 0;
            return true;
        }

        return false;
    }


}
