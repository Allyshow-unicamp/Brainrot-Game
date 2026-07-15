package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.brainrots.MainUX.MainUX;
import com.brainrots.core.BrainRotGame;
import com.brainrots.core.GameResourceManager;
import com.brainrots.wave.WaveManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

public class LevelManagerCoverageTest {

    private LevelManager levelManager;
    
    private MockedConstruction<Pixmap> mockedPixmap;
    private MockedConstruction<Texture> mockedTexture;
    private MockedConstruction<Map> mockedMap;
    private MockedConstruction<Base> mockedBase;
    private MockedConstruction<WaveManager> mockedWaveManager;
    private MockedConstruction<TowerPlacementController> mockedPlacementCtrl;

    @BeforeEach
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);

        mockedPixmap = Mockito.mockConstruction(Pixmap.class);
        mockedTexture = Mockito.mockConstruction(Texture.class);
        
        mockedMap = Mockito.mockConstruction(Map.class, (mock, context) -> {
            Mockito.when(mock.getLastPosition()).thenReturn(new Vector2(0, 0));
            Mockito.when(mock.getPath()).thenReturn(new Array<>());
        });
        
        mockedBase = Mockito.mockConstruction(Base.class);
        mockedWaveManager = Mockito.mockConstruction(WaveManager.class);
        mockedPlacementCtrl = Mockito.mockConstruction(TowerPlacementController.class);

        GameResourceManager resourceManager = mock(GameResourceManager.class);
        BrainRotGame screenManager = mock(BrainRotGame.class);
        OrthographicCamera camera = mock(OrthographicCamera.class);
        Stage stage = mock(Stage.class);
        MainUX mainUX = mock(MainUX.class, withSettings().lenient());

        levelManager = new LevelManager(
                resourceManager, screenManager, 1, "map_dummy", camera, stage, mainUX
        );
    }

    @AfterEach
    public void tearDown() {
        mockedPixmap.close();
        mockedTexture.close();
        mockedMap.close();
        mockedBase.close();
        mockedWaveManager.close();
        mockedPlacementCtrl.close();
    }

    @Test
    public void testGettersAndSettersForCoverage() {
        assertNotNull(levelManager.getActiveEnemies());
        assertNotNull(levelManager.getActiveTowers());
        assertNotNull(levelManager.getSynergyManager());
        assertNotNull(levelManager.getGameMode());
        
        levelManager.setSpeedMultiplier(3.0f);
        assertEquals(3.0f, levelManager.getSpeedMultiplier());
    }

    @Test
    public void testWaveStateMethodsForCoverage() {
        levelManager.pauseWaveGeneration();
        levelManager.resumeWaveGeneration();
        
        assertFalse(levelManager.isWavePaused());
    }

    @Test
    public void testTowerSelectionForCoverage() {
        levelManager.clearSelectedTower();
        assertNull(levelManager.getSelectedTower());
    }
}