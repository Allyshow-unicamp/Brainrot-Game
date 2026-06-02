package com.brainrots.screen;

import java.util.Vector;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brainrots.core.GameResourceManager;

/**
 * Classe usada como objetivo de auxiliar LevelManager. 
 * <p>Vai possuir os elementos principais pra uma boa UX</p>
 */
public class MainUX {
   

    //public static Texture iconeMoeda()
    //{
    //    
    //}

    public static Vector<Texture> getTowerIcon()
    {
        Vector<Texture> towerIcon = new Vector<>();
        GameResourceManager resourceManager = GameResourceManager.getGameResourceManager();

        towerIcon.add(resourceManager.getTexture("Towers/tung-tung-tung-sahur.png"));

        return towerIcon;
    }
}
