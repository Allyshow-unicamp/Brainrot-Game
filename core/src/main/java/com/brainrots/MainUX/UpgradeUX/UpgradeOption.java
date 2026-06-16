package com.brainrots.MainUX.UpgradeUX;


import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.brainrots.MainUX.TowerButton;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class UpgradeOption extends Table{
    
    private TowerButton upgradeButton;
    private Label currentLabel;
    private int upgradeCost;

    public UpgradeOption(TowerButton upgradeButton, int upgradeCost)
    {
        this.upgradeButton = upgradeButton;
        this.upgradeCost = upgradeCost;
        Label.LabelStyle textStyle = new Label.LabelStyle(new BitmapFont(),Color.WHITE);
        this.currentLabel = new Label(String.valueOf(upgradeCost),textStyle);

        createTable();
    }

    private void createTable()
    {
        this.add(upgradeButton).size(64,64).row();;
        this.add(currentLabel).padTop(2);
        this.pack(); 
        this.setTransform(true); 
        this.setOrigin(com.badlogic.gdx.utils.Align.center);
    }
}
