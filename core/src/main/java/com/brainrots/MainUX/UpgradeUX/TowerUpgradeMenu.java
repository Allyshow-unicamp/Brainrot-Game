package com.brainrots.MainUX.UpgradeUX;

import com.brainrots.tower.Tower;
import com.brainrots.tower.TowerEvolutionMap;
import com.brainrots.MainUX.MainUX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.brainrots.MainUX.TowerButton;
import com.badlogic.gdx.utils.Array;

import java.util.Vector;

public class TowerUpgradeMenu {
    
    private int options;
    private Vector<UpgradeOption> listOptions;
    private Group menuGroup; 
    private boolean isOpen;
    private float radius = 110f;
    private Texture circleTexture;

    public TowerUpgradeMenu()
    {
        this.listOptions = new Vector<UpgradeOption>();
        this.menuGroup = new Group();
        createCircleTexture();
    }

    private void createCircleTexture() {
        int size = (int)(radius * 2.6f);
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 1f);
        pixmap.fillCircle(size / 2, size / 2, size / 2);
        this.circleTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    public void openUpgradeMenu(Tower selectedTower, Stage stage, OrthographicCamera gameScreen, Array<Tower> activeTowers)
    {
        this.options = selectedTower.getNumberUpgrades();
        
        Image circleBackground = new Image(circleTexture);
        circleBackground.setColor(new Color(0.7f, 0.7f, 0.7f, 0.8f)); 
        
        float backgroundSize = selectedTower.getRange(); 
        circleBackground.setSize(backgroundSize, backgroundSize);
        circleBackground.setPosition(0, 0); 
        
        menuGroup.setSize(backgroundSize, backgroundSize);
        menuGroup.addActor(circleBackground); 
        
        float groupCenterX = backgroundSize / 2f;
        float groupCenterY = backgroundSize / 2f;

        for (int i = 0; i < options; i++) 
        {
            UpgradeOption upgradeButton = createUpgradeButton(
                selectedTower,TowerEvolutionMap.getTexture(selectedTower, i),i,activeTowers);
            
            float buttonXCoord = getButtonXCoord(i, groupCenterX, upgradeButton.getWidth());
            float buttonYCoord = getButtonYCoord(i, groupCenterY, upgradeButton.getHeight());
            upgradeButton.setPosition(buttonXCoord, buttonYCoord);

            menuGroup.addActor(upgradeButton);
            listOptions.add(upgradeButton);
        } 
        
        positionGroup(selectedTower, gameScreen, backgroundSize);
        stage.addActor(menuGroup);
    }

    public void closeUpgradeMenu() {
        menuGroup.clearChildren();
        menuGroup.remove();
        listOptions.clear(); 
    }

    private UpgradeOption createUpgradeButton(Tower selectedTower,
        Texture texture,int index,Array<Tower> activeTowers)
    {
        TowerButton upgradeButton = new TowerButton(
            texture, 0, 0, onClickAction(selectedTower, index, activeTowers));

        UpgradeOption option = new UpgradeOption(upgradeButton, 100);

        option.validate();
        return option;
    }

    private Runnable onClickAction(Tower selectedTower,int index, Array<Tower> activeTowers)
    {
        return () ->{
            if(MainUX.getCurrentCoins() < 100)
                return;
            Tower upgradedTower = TowerEvolutionMap.createNewTower(selectedTower, index);

            activeTowers.set(selectedTower.getID(), upgradedTower);
            closeUpgradeMenu();
        };
    }

   private void positionGroup(Tower tower, OrthographicCamera gameScreen, float backgroundSize)
    {
        float towerCenterX = tower.getPosition().x;
        float towerCenterY = tower.getPosition().y + (tower.getTowerHeight() / 2f);

        float jogoParaTelaX = (float) Gdx.graphics.getWidth() / gameScreen.viewportWidth;
        float jogoParaTelaY = (float) Gdx.graphics.getHeight() / gameScreen.viewportHeight;

        float stageX = towerCenterX * jogoParaTelaX;
        float stageY = towerCenterY * jogoParaTelaY;

        menuGroup.setPosition(stageX - (backgroundSize / 2f), stageY - (backgroundSize / 2f));
    }

    private float getButtonXCoord(int index, float centerX, float buttonWidth)
    {
        float angle = (index * (360f / options)) + 90f;
        float radian = angle * MathUtils.degreesToRadians;
        return centerX + (radius * MathUtils.cos(radian)) - buttonWidth/2;
    }

    private float getButtonYCoord(int index, float centerY, float buttonHeight)
    {
        float angle = (index * (360f / options)) + 90f;
        float radian = angle * MathUtils.degreesToRadians;
        return centerY + (radius * MathUtils.sin(radian)) - buttonHeight/2;
    }
}