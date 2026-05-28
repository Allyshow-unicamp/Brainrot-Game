package com.brainrots.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.core.GameResourceManager;

public class Map {
    private Array<Vector2> enemyPath;
    private Array<Rectangle> obstacleBounds;

    private TiledMap tiledMap;
    private Texture mapBackground;

    public Map(String mapName, GameResourceManager resourceManager){

        enemyPath = new Array<>();

        switch (mapName) {
            case "67Hills":
                    tiledMap = new TmxMapLoader().load("Maps/67Hills/67Hills.tmx");
                    mapBackground = resourceManager.getTexture("Maps/67Hills/67Hills.png");
                break;
            default:
                // fazer excecao;
                break;
        }

        Object pathObject = tiledMap.getLayers().get("path").getObjects().get(0);

        if(pathObject instanceof PolylineMapObject) {

            Polyline polyline = ((PolylineMapObject) pathObject).getPolyline();

            float[] vertices = polyline.getTransformedVertices();

            for(int i = 0; i < vertices.length; i += 2){
                float x = vertices [i];
                float y = vertices [i + 1];

                enemyPath.add(new Vector2(x, y));
                // System.out.println(new Vector2(x, y));
            }
        } else {
            // throws exception
        }
    }

    public Texture getBackground(){
        return mapBackground;
    }

    public Array<Vector2> getPath(){
        return enemyPath;
    }
}
