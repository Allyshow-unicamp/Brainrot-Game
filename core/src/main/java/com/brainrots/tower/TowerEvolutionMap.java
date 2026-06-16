package com.brainrots.tower;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

public class TowerEvolutionMap {

    private static TowerEvolutionMap towerEvolutionMap;
    private HashMap<Class<? extends Tower>,List<TempInterfaceTower>> mapOfEvolutions;

    private TowerEvolutionMap()
    {
        HashMap<Class<? extends Tower>,List<TempInterfaceTower>> newMap = new HashMap<>();
        newMap.put(ConcreteTower.class, List.of(new PatapimTower(null, 0)));

        this.mapOfEvolutions = newMap;
    }

    public static TowerEvolutionMap getTowerEvolutionMap()
    {
        if(towerEvolutionMap == null)
            towerEvolutionMap = new TowerEvolutionMap();
        return towerEvolutionMap;
    }
    
    public static Tower createNewTower(Tower tower, int index)
    {
        TowerEvolutionMap towerEvolutionMap = TowerEvolutionMap.getTowerEvolutionMap();

        TempInterfaceTower prototype = towerEvolutionMap.mapOfEvolutions.get(tower.getClass()).get(index);
        return prototype.createTower(tower.getPosition(), tower.getID());
    }

    public static Texture getTexture(Tower tower, int index)
    {
        TowerEvolutionMap towerEvolutionMap = TowerEvolutionMap.getTowerEvolutionMap();
        TempInterfaceTower prototype = towerEvolutionMap.mapOfEvolutions.get(tower.getClass()).get(index);
        return prototype.getTexture();
    }
}
