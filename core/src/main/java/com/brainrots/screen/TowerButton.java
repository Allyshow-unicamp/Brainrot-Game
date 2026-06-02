package com.brainrots.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TowerButton extends ImageButton{
    

    public TowerButton(Texture texture, float x, float y, Runnable onClick)
    {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        this.setPosition(x, y);
        this.setSize(64f,64f);
        this.clickEvent(onClick);
    }

    private void clickEvent(Runnable onClick)
    {
        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                onClick.run();
            }   
        });
    }
}
