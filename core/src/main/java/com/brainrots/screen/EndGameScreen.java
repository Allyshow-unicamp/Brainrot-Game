package com.brainrots.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.brainrots.core.Assets;
import com.brainrots.core.BrainRotGame;
import com.brainrots.interfaces.GameSaveHandler;
import com.brainrots.interfaces.ScreenManager.ScreenType;
import com.brainrots.core.PlayerProfile;
import com.brainrots.exception.PersistanceException;

/** Tela de fim de jogo. Exibe estrelas, recompensa e botão de retorno. */
public class EndGameScreen extends GameScreen {

    private boolean won;

    public EndGameScreen(SpriteBatch batch, BrainRotGame screenManager,
            ShapeRenderer shapeRenderer, int level, boolean won, int stars, GameSaveHandler gameSaveHandler) {
        super(batch, screenManager, shapeRenderer);
        this.won = won;

        if (won) {
            try {
                PlayerProfile playerProfile = screenManager.getPlayerProfile();

                int previousStars = playerProfile.getLevelStars().getOrDefault(String.valueOf(level), 0);
                if (stars > previousStars) {
                    playerProfile.setStars(String.valueOf(level), stars);
                }

                int coinReward = calculateCoinReward(stars);
                playerProfile.addGlobalCoins(coinReward);

                gameSaveHandler.saveProfile(playerProfile);
            } catch (PersistanceException e) {
                e.printStackTrace();
            }
        }

        Table table = createFullScreenTable();

        if(won){
            Label titleLabel = new Label("Victory", createLabelStyle(4.0f, Color.YELLOW));
            table.add(titleLabel).pad(60).row();
        } else{
            Label titleLabel = new Label("Defeat", createLabelStyle(4.0f, Color.YELLOW));
            table.add(titleLabel).pad(60).row();
        }


        if (won) {
            LabelStyle starsStyle = createLabelStyle(2.0f, Color.WHITE);
            Label starsLabel = new Label(stars + "/3 Stars", starsStyle);
            table.add(starsLabel).pad(20).row();

            int coinReward = calculateCoinReward(stars);
            Label coinsLabel = new Label("+" + coinReward + " Coins", starsStyle);
            coinsLabel.setColor(Color.YELLOW);
            table.add(coinsLabel).pad(20).row();
        }

        ButtonAssets buttons = createButtonAssets(2.0f);
        TextButton backButton = new TextButton("Back", buttons.style);
        table.add(backButton).size(200, 60);
        table.row();

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.switchScreen(ScreenType.LEVEL_SELECTION);
            }
        });

        stage.addActor(table);
    }

    private int calculateCoinReward(int stars) {
        switch (stars) {
            case 1: return 50;
            case 2: return 100;
            case 3: return 200;
            default: return 0;
        }
    }

    @Override
    protected void initializeHud() {

    }

    @Override
    protected void drawScreen(float delta) {

        if(!won){
            batch.begin();

            Texture texture = resourceManager.getTexture(Assets.DEFEAT_SCREEN);

            batch.draw(texture, 0, 0);
            batch.end();
        } else {
            batch.begin();

            Texture texture = resourceManager.getTexture(Assets.VICTORY_SCREEN);

            batch.draw(texture, 0, 0);
            batch.end();
        }

        drawWithStageHelper(delta);
    }
}
