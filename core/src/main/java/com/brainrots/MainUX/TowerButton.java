package com.brainrots.MainUX;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.brainrots.core.GameResourceManager;
import com.brainrots.tower.BrainrotTower;

import com.badlogic.gdx.graphics.Color;

/** Botão de seleção de torre na sidebar. Mostra textura e desabilita quando inacessível. */
public class TowerButton extends ImageButton{

    private BrainrotTower towerType;
    private Texture texture;

    public TowerButton(Texture texture, float x, float y, Runnable onClick)
    {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        this.setPosition(x, y);
        this.setSize(64f, 64f);

        this.texture = texture;
    }

    public TowerButton(BrainrotTower towerType, float x, float y)
    {
        super(new TextureRegionDrawable(new TextureRegion(
            GameResourceManager.getGameResourceManager().getTexture(towerType.getTexture()))));
        this.setPosition(x, y);
        this.setSize(64f, 64f);

        this.towerType = towerType;
        this.texture = GameResourceManager.getGameResourceManager().getTexture(towerType.getTexture());
    }

    public BrainrotTower getTowerType(){
        return towerType;
    }

    public Texture getTexture(){
        return texture;
    }

    public void setClickEvent(Runnable onClick)
    {
        this.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!isDisabled()) {
                    onClick.run();
                }
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                if (!isDisabled()) {
                    onClick.run();
                }
            }
        });
    }

    public void setAffordable(boolean affordable) {
        if (affordable) {
            setColor(Color.WHITE);
        } else {
            setColor(0.4f, 0.4f, 0.4f, 0.6f);
        }
    }
}
