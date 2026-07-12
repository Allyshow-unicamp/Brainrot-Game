package com.brainrots.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;

public class Map {
    private final Array<Vector2> enemyPath = new Array<>();
    private final Array<Polygon> barrier = new Array<>();

    private TiledMap tiledMap;
    private Texture mapBackground;

    public Map(String mapName, GameResourceManager resourceManager) {
        switch (mapName) {
            case Assets.MAP_67_HILLS_ID:
                tiledMap = new TmxMapLoader().load(Assets.MAP_67_HILLS_TMX);
                mapBackground = resourceManager.getTexture(Assets.MAP_67_HILLS_BG);
                break;
            case Assets.MAP_SIGMA_VOLCANO_ID:
                tiledMap = new TmxMapLoader().load(Assets.MAP_SIGMA_VOLCANO_TMX);
                mapBackground = resourceManager.getTexture(Assets.MAP_SIGMA_VOLCANO_BG);
                break;
            case Assets.MAP_MINING_RIZZ_ID:
                tiledMap = new TmxMapLoader().load(Assets.MAP_MINING_RIZZ_TMX);
                mapBackground = resourceManager.getTexture(Assets.MAP_MINING_RIZZ_BG);
                break;
            case Assets.MAP_BETA_JUNGLE_ID:
                tiledMap = new TmxMapLoader().load(Assets.MAP_BETA_JUNGLE_TMX);
                mapBackground = resourceManager.getTexture(Assets.MAP_BETA_JUNGLE_BG);
                break;
            case Assets.MAP_WINTER_AURA_ID:
                tiledMap = new TmxMapLoader().load(Assets.MAP_WINTER_AURA_TMX);
                mapBackground = resourceManager.getTexture(Assets.MAP_WINTER_AURA_BG);
                break;
            default:
                System.err.println("Mapa não existe");
                return;
        }

        Object pathObject = tiledMap.getLayers().get("Path").getObjects().get(0);

        if (pathObject instanceof PolylineMapObject polylineObject) {
            Polyline polyline = polylineObject.getPolyline();
            float[] vertices = polyline.getTransformedVertices();

            for (int i = 0; i < vertices.length; i += 2) {
                enemyPath.add(new Vector2(vertices[i], vertices[i + 1]));
            }
        } else {
            System.err.println("Caminho não é instância de PolylineMapObject");
        }

        for(Object object : tiledMap.getLayers().get("Barrier").getObjects()){

            if (object instanceof PolygonMapObject polygon) {
                
                barrier.add(polygon.getPolygon());
            }
        }

    }

    public Texture getBackground() {
        return mapBackground;
    }

    public Array<Vector2> getPath() {
        return enemyPath;
    }

    public Array<Polygon> getBarrier(){
        return barrier;
    }

    public Vector2 getLastPosition() {
        return enemyPath.get(enemyPath.size - 1);
    }
}
