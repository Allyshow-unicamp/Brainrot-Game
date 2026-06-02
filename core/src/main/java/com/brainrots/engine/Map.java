package com.brainrots.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.Assets;
import com.brainrots.core.GameResourceManager;

public class Map {
    private final Array<Vector2> enemyPath = new Array<>();

    private TiledMap tiledMap;
    private Texture mapBackground;

    public Map(String mapName, GameResourceManager resourceManager) {
        switch (mapName) {
            case Assets.MAP_67_HILLS_ID:
                tiledMap = new TmxMapLoader().load(Assets.MAP_67_HILLS_TMX);
                mapBackground = resourceManager.getTexture(Assets.MAP_67_HILLS_BG);
                break;
            default:
                System.err.println("Mapa não existe");
                break;
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
    }

    public Texture getBackground() {
        return mapBackground;
    }

    public Array<Vector2> getPath() {
        return enemyPath;
    }

    public Vector2 getLastPosition() {
        return enemyPath.get(enemyPath.size - 1);
    }
}
