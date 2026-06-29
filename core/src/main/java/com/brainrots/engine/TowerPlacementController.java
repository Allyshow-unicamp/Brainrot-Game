package com.brainrots.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.brainrots.MainUX.MainUX;
import com.brainrots.tower.BrainrotTower;
import com.brainrots.tower.Tower;
import com.brainrots.tower.TowerArea;
import com.brainrots.tower.TowerBoost;
import com.brainrots.tower.TowerCoin;
import com.brainrots.tower.TowerProjArea;
import com.brainrots.tower.TowerProjSingle;

public class TowerPlacementController {

    private static final float DRAG_THRESHOLD = 20f;
    private static final int TOWER_SIZE = 150;

    private BrainrotTower selectedTowerType;
    private Texture towerGhostTexture;
    private boolean isIconSelected;
    private boolean wasMousePressed;
    private boolean isMousePressed;
    private boolean isDragging;
    private boolean ghostRed;
    private Vector2 dragStartScreenPos;
    private OrthographicCamera gameScreen;
    private Stage stage;

    public TowerPlacementController(OrthographicCamera gameScreen, Stage stage) {
        this.gameScreen = gameScreen;
        this.stage = stage;
        this.wasMousePressed = false;
        this.isMousePressed = false;
        this.dragStartScreenPos = new Vector2();
        this.isDragging = false;
        this.ghostRed = false;
    }

    public void selectTower(BrainrotTower towerType, Texture ghostTexture) {
        this.selectedTowerType = towerType;
        this.towerGhostTexture = ghostTexture;
        this.isIconSelected = true;
        this.isDragging = false;
        this.dragStartScreenPos.set(Gdx.input.getX(), Gdx.input.getY());
    }

    public void update(Array<Polygon> barriers, Array<Tower> activeTowers, CoinManager coinManager, MainUX mainUX) {
        wasMousePressed = isMousePressed;
        boolean leftDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightDown = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        isMousePressed = leftDown || rightDown;

        if (isIconSelected) {
            float dx = Gdx.input.getX() - dragStartScreenPos.x;
            float dy = Gdx.input.getY() - dragStartScreenPos.y;
            if (dx * dx + dy * dy > DRAG_THRESHOLD * DRAG_THRESHOLD) {
                isDragging = true;
            }
        }

        checkPositionTower(barriers, activeTowers, coinManager, mainUX);
    }

    public void updateGhostState(Array<Polygon> barriers, CoinManager coinManager) {
        if (!isIconSelected || selectedTowerType == null) return;

        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.unproject(mousePosition);

        boolean canAfford = coinManager.canAfford(selectedTowerType.getCost());
        boolean onBarrier = false;

        for (Polygon barrier : barriers) {
            if (barrier.contains(mousePosition.x, mousePosition.y)) {
                onBarrier = true;
                break;
            }
        }

        ghostRed = onBarrier || !canAfford;
    }

    public void renderGhost(SpriteBatch batch) {
        if (isIconSelected && towerGhostTexture != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.unproject(mousePosition);

            if (ghostRed) {
                batch.setColor(1, 0, 0, 0.5f);
            } else {
                batch.setColor(1, 1, 1, 0.4f);
            }

            batch.draw(towerGhostTexture, mousePosition.x - TOWER_SIZE / 2,
                mousePosition.y, TOWER_SIZE, TOWER_SIZE);

            batch.setColor(1, 1, 1, 1.0f);
        }
    }

    public boolean isPlacingTower() {
        return isIconSelected;
    }

    public void cancelPlacement() {
        isIconSelected = false;
        towerGhostTexture = null;
        selectedTowerType = null;
        isDragging = false;
    }

    private void checkPositionTower(Array<Polygon> barriers, Array<Tower> activeTowers,
                                     CoinManager coinManager, MainUX mainUX) {
        if (!isIconSelected) return;

        boolean leftClick = Gdx.input.justTouched() &&
            Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightClick = Gdx.input.justTouched() &&
            Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        boolean dragEnd = isDragging && wasMousePressed && !isMousePressed;

        if (!leftClick && !rightClick && !dragEnd) return;

        if (isCursorOverUI()) return;

        Vector3 currentMouseWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.unproject(currentMouseWorld);
        Vector2 worldPos = new Vector2(currentMouseWorld.x, currentMouseWorld.y);

        placeTowerAtPosition(worldPos, barriers, activeTowers, coinManager, mainUX);
    }

    private void placeTowerAtPosition(Vector2 worldPosition, Array<Polygon> barriers,
                                       Array<Tower> activeTowers, CoinManager coinManager, MainUX mainUX) {
        if (selectedTowerType == null) return;

        if (!coinManager.canAfford(selectedTowerType.getCost())) {
            cancelPlacement();
            return;
        }

        for (Polygon barrier : barriers) {
            if (barrier.contains(worldPosition)) {
                return;
            }
        }

        coinManager.subtractCoins(selectedTowerType.getCost());
        mainUX.updateCoins(coinManager.getCoins());

        Tower tower = createTowerFromType(selectedTowerType, towerGhostTexture, worldPosition);
        if (tower != null) {
            activeTowers.add(tower);
        }

        cancelPlacement();
    }

    private Tower createTowerFromType(BrainrotTower type, Texture texture, Vector2 position) {
        switch (type) {
            case TUNG_TUNG:
                return new TowerProjSingle(texture, position,
                    type.getDisplayName(), type.getDamage(), type.getAttackSpeed(),
                    type.getRange(), type.getAttackCooldown(),
                    TOWER_SIZE, TOWER_SIZE, 0);
            case BOMBARDILO_CROCODILO:
                return new TowerProjArea(texture, position,
                    type.getDisplayName(), type.getDamage(), type.getAttackSpeed(),
                    type.getRange(), type.getAttackCooldown(),
                    TOWER_SIZE, TOWER_SIZE, 0, type.getExtraFloat());
            case NUCLEARO_DINOSAURO:
                return new TowerArea(texture, position,
                    type.getDisplayName(), type.getDamage(),
                    type.getRange(), type.getAttackCooldown(),
                    TOWER_SIZE, TOWER_SIZE, 0);
            case CAPPUCCINO_ASSASSINO:
                return new TowerCoin(texture, position,
                    type.getDisplayName(),
                    type.getRange(), type.getAttackCooldown(),
                    TOWER_SIZE, TOWER_SIZE, 0, type.getExtraValue());
            case BALLERINA_CAPPUCCINA:
                return new TowerBoost(texture, position,
                    type.getDisplayName(),
                    type.getRange(), type.getAttackCooldown(),
                    TOWER_SIZE, TOWER_SIZE, 0, type.getExtraValue());
            default:
                return null;
        }
    }

    private boolean isCursorOverUI() {
        Vector2 stageCoords = stage.screenToStageCoordinates(
            new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Actor hitActor = stage.hit(stageCoords.x, stageCoords.y, true);
        return hitActor != null;
    }
}
