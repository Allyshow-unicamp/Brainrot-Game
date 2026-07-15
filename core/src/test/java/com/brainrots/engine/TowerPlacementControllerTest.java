package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.brainrots.MainUX.MainUX;
import com.brainrots.tower.BrainrotTower;
import com.brainrots.tower.Tower;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TowerPlacementControllerTest {

    private TowerPlacementController controller;
    private OrthographicCamera camera;
    private Stage stage;
    private CoinManager coinManager;
    private MainUX mainUX;
    private Texture dummyTexture;

    @BeforeAll
    public static void loadNatives() {
        GdxNativesLoader.load();
    }

    @BeforeEach
    public void setUp() {
        Gdx.input = mock(Input.class);
        when(Gdx.input.getX()).thenReturn(0);
        when(Gdx.input.getY()).thenReturn(0);

        Gdx.graphics = mock(Graphics.class);
        when(Gdx.graphics.getWidth()).thenReturn(800);
        when(Gdx.graphics.getHeight()).thenReturn(600);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        camera.update();
        
        stage = mock(Stage.class);
        when(stage.screenToStageCoordinates(any(Vector2.class))).thenReturn(new Vector2(0, 0));
        when(stage.hit(anyFloat(), anyFloat(), anyBoolean())).thenReturn(null);

        coinManager = mock(CoinManager.class);
        mainUX = mock(MainUX.class);
        dummyTexture = mock(Texture.class);

        controller = new TowerPlacementController(camera, stage);
    }

    @AfterEach
    public void tearDown() {
        Gdx.input = null;
        Gdx.graphics = null;
    }

    @Test
    public void testSelectAndCancelTower() {
        assertFalse(controller.isPlacingTower(), "Não deve iniciar colocando torre");

        controller.selectTower(BrainrotTower.TUNG_TUNG, dummyTexture);
        assertTrue(controller.isPlacingTower(), "Após selecionar, deve estar colocando torre");

        controller.cancelPlacement();
        assertFalse(controller.isPlacingTower(), "Ao cancelar, o estado deve voltar a falso");
    }

    @Test
    public void testPlacementDeniedWhenNotEnoughCoins() {
        controller.selectTower(BrainrotTower.TUNG_TUNG, dummyTexture);
        
        when(Gdx.input.justTouched()).thenReturn(true);
        when(Gdx.input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(true);
        when(coinManager.canAfford(anyInt())).thenReturn(false);

        Array<Tower> activeTowers = new Array<>();
        controller.update(new Array<>(), new Array<>(), activeTowers, coinManager, mainUX);

        assertEquals(0, activeTowers.size, "Nenhuma torre deve ser adicionada à lista");
        assertFalse(controller.isPlacingTower(), "A colocação deve ser cancelada por falta de dinheiro");
    }

    @Test
    public void testRightClickCancelsPlacement() {
        controller.selectTower(BrainrotTower.TUNG_TUNG, dummyTexture);
        
        when(Gdx.input.justTouched()).thenReturn(true);
        when(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)).thenReturn(true);

        controller.update(new Array<>(), new Array<>(), new Array<>(), coinManager, mainUX);

        assertFalse(controller.isPlacingTower(), "O clique direito deve cancelar a operação");
    }

    @Test
    public void testPlacementDeniedWhenAreaIsInvalid() {
        controller.selectTower(BrainrotTower.TUNG_TUNG, dummyTexture);
        
        when(coinManager.canAfford(anyInt())).thenReturn(true);
        when(Gdx.input.justTouched()).thenReturn(true);
        when(Gdx.input.isButtonPressed(Input.Buttons.LEFT)).thenReturn(true);

        Array<Polygon> barriers = new Array<>();
        Polygon barrier = new Polygon(new float[]{ 
            -1000, -1000, 
             1000, -1000, 
             1000,  1000, 
            -1000,  1000 
        });
        barriers.add(barrier);

        Array<Tower> activeTowers = new Array<>();
        controller.update(barriers, new Array<>(), activeTowers, coinManager, mainUX);

        assertEquals(0, activeTowers.size, "Não deve permitir criar torre em cima da barreira");
        assertTrue(controller.isPlacingTower(), "O jogador continua segurando a torre para tentar em outro lugar");
    }

    @Test
    public void testRenderGhostCallsBatchDraw() {
        SpriteBatch batch = mock(SpriteBatch.class);
        controller.selectTower(BrainrotTower.TUNG_TUNG, dummyTexture);
        
        when(coinManager.canAfford(anyInt())).thenReturn(true);
        
        controller.updateGhostState(new Array<>(), new Array<>(), coinManager);
        controller.renderGhost(batch);

        verify(batch).draw(eq(dummyTexture), anyFloat(), anyFloat(), anyFloat(), anyFloat());
    }
}