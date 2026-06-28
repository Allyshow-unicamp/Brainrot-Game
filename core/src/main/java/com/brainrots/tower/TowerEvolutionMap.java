package com.brainrots.tower;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;

public class TowerEvolutionMap {

    private static TowerEvolutionMap towerEvolutionMap;
    private HashMap<String, List<TempInterfaceTower>> mapOfEvolutions;

    private TowerEvolutionMap()
    {
        HashMap<String, List<TempInterfaceTower>> newMap = new HashMap<>();
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

        String key = tower.getClass().getSimpleName();
        List<TempInterfaceTower> evolutions = towerEvolutionMap.mapOfEvolutions.get(key);
        if (evolutions == null || index >= evolutions.size()) return tower;

        TempInterfaceTower prototype = evolutions.get(index);
        return prototype.createTower(tower.getPosition(), tower.getID());
    }

    public static Texture getTexture(Tower tower, int index)
    {
        TowerEvolutionMap towerEvolutionMap = TowerEvolutionMap.getTowerEvolutionMap();

        String key = tower.getClass().getSimpleName();
        List<TempInterfaceTower> evolutions = towerEvolutionMap.mapOfEvolutions.get(key);
        if (evolutions == null || index >= evolutions.size()) return null;

        TempInterfaceTower prototype = evolutions.get(index);
        return prototype.getTexture();
    }
}
