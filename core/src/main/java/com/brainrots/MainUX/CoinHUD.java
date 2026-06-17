package com.brainrots.MainUX;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CoinHUD extends Table {

    private Image coinImage;
    private Label coinLabel;
    private int currentCoins;

    public CoinHUD(Texture coinTexture, int initialCoins) {
        this.currentCoins = initialCoins;
        this.coinImage = new Image(coinTexture);

        Label.LabelStyle textStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        this.coinLabel = new Label(String.valueOf(currentCoins), textStyle);

        this.add(coinImage).size(32, 32).padRight(5);
        this.add(coinLabel);

        this.pack(); 
    }

    /**
     * Método para atualizar o contador de moedas de fora
     */
    public void updateCoins(int newCoinCount) {
        if (this.currentCoins != newCoinCount) {
            this.currentCoins = newCoinCount;
            this.coinLabel.setText(String.valueOf(newCoinCount));
            this.pack(); 
        }
    }

    public int getCurrentCoins() {
        return currentCoins;
    }
}
