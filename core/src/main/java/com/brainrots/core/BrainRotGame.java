package com.brainrots.core;

import java.nio.file.Paths;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.brainrots.interfaces.GameSaveHandler;
import com.brainrots.interfaces.ScreenManager;
import com.brainrots.screen.EndGameScreen;
import com.brainrots.screen.GameplayScreen;
import com.brainrots.screen.GlobalShopScreen;
import com.brainrots.screen.LevelSelectionScreen;
import com.brainrots.screen.MainMenuScreen;

/**
 * Classe que gerencia todo o jogo.
 */
public class BrainRotGame extends Game implements ScreenManager {

    private SpriteBatch batch;
    private GameResourceManager resourceManager;
    private ShapeRenderer shapeRenderer;
    private PlayerProfile playerProfile;
    private GameSaveHandler gameSaveHandler;

    /**
     * Retorna os dados do jogador.
     * @return PlayerProfile contendo os dados.
     */
    public PlayerProfile getPlayerProfile() {
        return playerProfile;
    }

    /**
     * Inicialize gerenciadores de sprite.
     */
    private void initializeSprites(){
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        resourceManager = GameResourceManager.getGameResourceManager();
        resourceManager.loadAssets();
    }

    /*
    * Inicia a musica do menu principal.
    */
    private void playMenuSound(){
        Music backgroundMusic = resourceManager.getSound(Assets.MUSIC_GAME);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();
    }

    /*
    * Inicializa os dados do jogador.
    */
    private void loadPlayerProfile(){
        String profilePath = Paths.get("data", "player-data.json").toString();
        String shopPath = Paths.get("data", "shop-data.json").toString();
        String levelPath = Paths.get("data", "level-data.json").toString();

        gameSaveHandler = new JsonGameSaveHandler(profilePath, shopPath, levelPath);
        try {
            this.playerProfile = gameSaveHandler.loadProfile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Salva os dados do jogador.
     */
    private void savePlayerProfile(){
        try {
            gameSaveHandler.saveProfile(playerProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Altera entre telas do jogo.
     * @param type ScreenType com a tela para trocar.
     */
    public void switchScreen(ScreenType type) {
        switch (type) {
            case MAIN_MENU:
                setScreen(new MainMenuScreen(batch, this, shapeRenderer));
                break;
            case GLOBAL_SHOP:
                setScreen(new GlobalShopScreen(batch, this, shapeRenderer, gameSaveHandler));
                break;
            case LEVEL_SELECTION:
                setScreen(new LevelSelectionScreen(batch, this, shapeRenderer, gameSaveHandler));
                break;
            default:
                break;
        }
    }

    /**
     * Finaliza o programa limpando a memória.
     */
    public void endProgram(){
        Gdx.app.exit();
    }

    /**
     * Inicializa o jogo.
     */
    @Override
    public void create() {

        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());

        loadPlayerProfile();

        initializeSprites();

        playMenuSound();

        switchScreen(ScreenType.MAIN_MENU);
    }

    /**
     * Finaliza o jogo.
     */
    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        resourceManager.dispose();
        batch.dispose();

        savePlayerProfile();
    }


    public void selectLevel(int level, String levelName) {
        setScreen(new GameplayScreen(batch, this, shapeRenderer, level, levelName));
    }

    public void endGame(int level, boolean won, int stars) {
        setScreen(new EndGameScreen(batch, this, shapeRenderer, level, won, stars, gameSaveHandler));
    }

    /**
     * Inicia o modo infinito para o nível especificado.
     * Cria um novo GameplayScreen configurado com InfiniteMode.
     * A estratégia de waves começa como FiniteWaveStrategy (waves pré-definidas)
     * e o InfiniteMode troca para InfiniteWaveStrategy automaticamente quando
     * as waves finitas terminam.
     *
     * @param level     número do nível
     * @param levelName nome do mapa (ex: "67Hills")
     */
    public void switchToInfiniteMode(int level, String levelName) {
        GameplayScreen gameplayScreen = new GameplayScreen(batch, this, shapeRenderer, level, levelName);
        gameplayScreen.getLevelManager().setGameMode(new com.brainrots.engine.mode.InfiniteMode());
        setScreen(gameplayScreen);
    }
}
