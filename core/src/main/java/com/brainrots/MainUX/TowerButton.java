package com.brainrots.MainUX;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TowerButton extends ImageButton{
    
    private Texture texture;

    public TowerButton(Texture texture, float x, float y, Runnable onClick)
    {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        this.setPosition(x, y);
        this.setSize(64f,64f);

        this.texture = texture;
    }

    public Texture getTexture(){
        return texture;
    }

    public void setClickEvent(Runnable onClick)
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
