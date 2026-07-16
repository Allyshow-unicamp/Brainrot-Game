package com.brainrots.interfaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

/** Utilitário para atualização segura de Stage com delta clamped (1/30s). */
public final class StageHelper {
    private static final float MAX_DELTA = 1f / 30f;

    public static void act(Stage stage) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), MAX_DELTA));
    }
}
