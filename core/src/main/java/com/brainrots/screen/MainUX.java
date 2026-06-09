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
import com.brainrots.engine.CoinHUD;

/**
 * Classe usada como objetivo de auxiliar LevelManager. 
 * <p>Vai possuir os elementos principais pra uma boa UX</p>
 */
public class MainUX {

    private static MainUX mainUX;
    private CoinHUD coinHUD;

    public static MainUX getMainUX() {
        if (mainUX == null) {
            mainUX = new MainUX();
        }
        return mainUX;
    }

    public static Vector<Texture> getTowerIcon()
    {
        Vector<Texture> towerIcon = new Vector<>();
        GameResourceManager resourceManager = GameResourceManager.getGameResourceManager();

        towerIcon.add(resourceManager.getTexture("Towers/tung-tung-tung-sahur.png"));

        return towerIcon;
    }

    public static Texture getCoinTexture()
    {
        GameResourceManager resourceManager = GameResourceManager.getGameResourceManager();

        return resourceManager.getTexture("CCoins/coins.png");

    }

    public void registerCoinHUD(CoinHUD hud) {
        this.coinHUD = hud;
    }

    public static void updateCoins(int newCoinCount) {
        if (getMainUX().coinHUD != null) {
            getMainUX().coinHUD.updateCoins(newCoinCount);
        }
    }

    public void clear() {
        this.coinHUD = null;
    }

}
