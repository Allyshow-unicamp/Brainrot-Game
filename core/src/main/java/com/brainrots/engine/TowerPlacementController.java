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
import com.brainrots.core.GameResourceManager;
import com.brainrots.tower.BrainrotTower;
import com.brainrots.tower.Tower;
import com.brainrots.tower.TowerArea;
import com.brainrots.tower.TowerBoost;
import com.brainrots.tower.TowerCoin;
import com.brainrots.tower.TowerProjArea;
import com.brainrots.tower.TowerProjSingle;
import com.brainrots.core.GameBalance;

/** Controla posicionamento de torres: drag/click, ghost preview, validação de área e criação. */
public class TowerPlacementController {
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

    public void update(Array<Polygon> barriers, Array<Vector2> path, Array<Tower> activeTowers, CoinManager coinManager, MainUX mainUX) {
        wasMousePressed = isMousePressed;
        boolean leftDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightDown = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        isMousePressed = leftDown || rightDown;

        if (isIconSelected) {
            float dx = Gdx.input.getX() - dragStartScreenPos.x;
            float dy = Gdx.input.getY() - dragStartScreenPos.y;
            if (dx * dx + dy * dy > GameBalance.DRAG_THRESHOLD * GameBalance.DRAG_THRESHOLD) {
                isDragging = true;
            }
        }

        updateGhostState(barriers, path, coinManager);
        checkPositionTower(barriers, path, activeTowers, coinManager, mainUX);
    }

    public void updateGhostState(Array<Polygon> barriers, Array<Vector2> path, CoinManager coinManager) {
        if (!isIconSelected || selectedTowerType == null) return;

        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.unproject(mousePosition);
        Vector2 worldPos = new Vector2(mousePosition.x, mousePosition.y - GameBalance.TOWER_SIZE / 2f);

        boolean canAfford = coinManager.canAfford(selectedTowerType.getCost());
        boolean areaValid = isAreaValid(worldPos, barriers, path);

        ghostRed = !areaValid || !canAfford;
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

            batch.draw(towerGhostTexture, mousePosition.x - GameBalance.TOWER_SIZE / 2f,
                mousePosition.y - GameBalance.TOWER_SIZE / 2f, GameBalance.TOWER_SIZE, GameBalance.TOWER_SIZE);

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

    /* Posiciona torre ao soltar drag (ou clicar) em área válida; right-click cancela */
    private void checkPositionTower(Array<Polygon> barriers, Array<Vector2> path, Array<Tower> activeTowers,
                                     CoinManager coinManager, MainUX mainUX) {
        if (!isIconSelected) return;

        boolean leftClick = Gdx.input.justTouched() &&
            Gdx.input.isButtonPressed(Input.Buttons.LEFT);
        boolean rightClick = Gdx.input.justTouched() &&
            Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        boolean dragEnd = isDragging && wasMousePressed && !isMousePressed;

        if(rightClick)
        {
            cancelPlacement();
            return;
        }

        if (!leftClick && !rightClick && !dragEnd) return;

        if (isCursorOverUI()) return;

        Vector3 currentMouseWorld = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        gameScreen.unproject(currentMouseWorld);
        Vector2 worldPos = new Vector2(currentMouseWorld.x, currentMouseWorld.y - GameBalance.TOWER_SIZE / 2f);

        placeTowerAtPosition(worldPos, barriers, path, activeTowers, coinManager, mainUX);
    }

    private void placeTowerAtPosition(Vector2 worldPosition, Array<Polygon> barriers, Array<Vector2> path,
                                       Array<Tower> activeTowers, CoinManager coinManager, MainUX mainUX) {
        if (selectedTowerType == null) return;

        if (!coinManager.canAfford(selectedTowerType.getCost())) {
            cancelPlacement();
            return;
        }

        if (!isAreaValid(worldPosition, barriers, path)) {
            return;
        }

        coinManager.subtractCoins(selectedTowerType.getCost());
        mainUX.updateCoins(coinManager.getCoins());

        Tower tower = createTowerFromType(selectedTowerType, towerGhostTexture, worldPosition);
        if (tower != null) {
            activeTowers.add(tower);
        }

        cancelPlacement();
    }

    /* Amostra pontos numa grade sobre a área da torre: se >= 67% estiverem válidos, posição é aceita */
    private boolean isAreaValid(Vector2 worldPos, Array<Polygon> barriers, Array<Vector2> path) {
        float halfSize = GameBalance.TOWER_SIZE / 2f;
        int samplePoints = GameBalance.SAMPLE_GRID;
        int totalPoints = samplePoints * samplePoints;
        int validPoints = 0;

        for (int xi = 0; xi < samplePoints; xi++) {
            for (int yi = 0; yi < samplePoints; yi++) {
                float px = worldPos.x - halfSize + (xi * GameBalance.TOWER_SIZE / (samplePoints - 1));
                float py = worldPos.y + (yi * GameBalance.TOWER_SIZE / (samplePoints - 1));
                Vector2 point = new Vector2(px, py);

                boolean inForbidden = false;
                for (Polygon barrier : barriers) {
                    if (barrier.contains(point)) {
                        inForbidden = true;
                        break;
                    }
                }
                if (!inForbidden && isPointNearPath(point, path, GameBalance.PATH_THRESHOLD)) {
                    inForbidden = true;
                }

                if (!inForbidden) {
                    validPoints++;
                }
            }
        }

        return (float) validPoints / totalPoints >= GameBalance.VALID_THRESHOLD;
    }

    private boolean isPointNearPath(Vector2 point, Array<Vector2> path, float threshold) {
        for (int i = 0; i < path.size - 1; i++) {
            Vector2 a = path.get(i);
            Vector2 b = path.get(i + 1);
            float dist = distanceToLineSegment(point, a, b);
            if (dist <= threshold) return true;
        }
        return false;
    }

    /* Projeta ponto p no segmento a-b e retorna a distância (usa t clampado entre 0 e 1) */
    private float distanceToLineSegment(Vector2 p, Vector2 a, Vector2 b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        float lengthSq = dx * dx + dy * dy;
        if (lengthSq == 0) return p.dst(a);

        float t = ((p.x - a.x) * dx + (p.y - a.y) * dy) / lengthSq;
        t = Math.max(0, Math.min(1, t));

        float projX = a.x + t * dx;
        float projY = a.y + t * dy;
        return p.dst(projX, projY);
    }

    private Tower createTowerFromType(BrainrotTower type, Texture texture, Vector2 position) {
        Texture synergyTexture = null;
        if (type.getSynergyTexture() != null) {
            synergyTexture = GameResourceManager.getGameResourceManager().getTexture(type.getSynergyTexture());
        }

        Tower tower;
        switch (type) {
            case TUNG_TUNG:
                tower = new TowerProjSingle(texture,synergyTexture ,position,
                    type.getDisplayName(), type.getDamage(), type.getAttackSpeed(),
                    type.getRange(), type.getAttackCooldown(),
                    GameBalance.TOWER_SIZE, GameBalance.TOWER_SIZE, type);
                break;
            case BOMBARDILO_CROCODILO:
                tower = new TowerProjArea(texture,synergyTexture ,position,
                    type.getDisplayName(), type.getDamage(), type.getAttackSpeed(),
                    type.getRange(), type.getAttackCooldown(),
                    GameBalance.TOWER_SIZE, GameBalance.TOWER_SIZE, type, type.getExtraFloat());
                break;
            case NUCLEARO_DINOSAURO:
                tower = new TowerArea(texture,synergyTexture ,position,
                    type.getDisplayName(), type.getDamage(),
                    type.getRange(), type.getAttackCooldown(),
                    GameBalance.TOWER_SIZE, GameBalance.TOWER_SIZE, type);
                break;
            case CAPPUCCINO_ASSASSINO:
                tower = new TowerCoin(texture, synergyTexture,position,
                    type.getDisplayName(),
                    type.getRange(), type.getAttackCooldown(),
                    GameBalance.TOWER_SIZE, GameBalance.TOWER_SIZE, type, type.getExtraValue());
                break;
            case BALLERINA_CAPPUCCINA:
                tower = new TowerBoost(texture, synergyTexture, position,
                    type.getDisplayName(),
                    type.getRange(), type.getAttackCooldown(),
                    GameBalance.TOWER_SIZE, GameBalance.TOWER_SIZE, type, type.getExtraValue());
                break;
            default:
                return null;
        }
        tower.setDescription(type.getDescription());
        return tower;
    }

    /** Verifica se o clique não foi em ator UI (evita conflito stage/game). */
    private boolean isCursorOverUI() {
        Vector2 stageCoords = stage.screenToStageCoordinates(
            new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        Actor hitActor = stage.hit(stageCoords.x, stageCoords.y, true);
        return hitActor != null;
    }
}
