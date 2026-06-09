package com.brainrots.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;


public class EnemyFactory {

    private static EnemyFactory enemyFactory;
    private HashMap<String,Spawnable> mapOfEnemies;

    /**
     * Defino prototipos de iniimgos, que vou usar para spawnar novos.
     * Facilita no proprio processamento do jogo
     */
    private EnemyFactory()
    {
        HashMap<String,Spawnable> newMap = new HashMap<>();
        newMap.put("67_Ball",new Ball_67(null));
        newMap.put("6_Ball", new Ball_6(null));
        newMap.put("7_Ball",new Ball_7(null));
        newMap.put("6_7_Ball",new Ball_6_7(null));

        this.mapOfEnemies = newMap;
    }

    /**
     * Metodo singleton, pois existe uma unica EnemyFactory no jogo
     * @return retorna a instancia pro Singleton
     */
    public static EnemyFactory getEnemyFactory()
    {
        if(enemyFactory ==null)
            enemyFactory = new EnemyFactory();
        return enemyFactory;
    }

    /**
     * Metodo que spawna formalmente novos inimigos no mapa
     * @param enemy nome do inimigo que quer spawnar
     * @param path caminho do inimigo no mapa
     * @return retorna o inimigo em si
     */
    public Enemy spawnEnemy(String enemy, Array<Vector2> path){
        Spawnable prototype = this.mapOfEnemies.get(enemy);

        return prototype.spawn(path);
    }
    
}
