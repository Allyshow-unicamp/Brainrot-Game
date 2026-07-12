package com.brainrots.tower;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.brainrots.effect.StatusEffect;
import com.brainrots.enemy.Enemy;
import com.brainrots.engine.CoinManager;
import com.brainrots.engine.ProjectileManager;
import com.brainrots.entity.GameObject;
import com.brainrots.synergy.SynergyBoost;
import com.brainrots.synergy.SynergyGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Tower extends GameObject {

    private String id;
    private String displayName;
    private String description;
    private TextureRegion textureRegion;
    protected Texture synergyTexture;
    private Texture originalTexture;
    protected int damage;
    protected int attackSpeed;
    protected float range;
    protected float attackCooldown;
    private float lastAttackTime;
    protected int towerID;
    protected int bonusDamage;
    protected BrainrotTower towerType;
    protected int valueSpent;
    protected int damageUpgradeLevel;
    protected int rangeUpgradeLevel;
    protected int cooldownUpgradeLevel;

    private List<SynergyBoost> activeSynergyBoosts;

    private static final int BASE_DAMAGE_COST = 50;
    private static final int BASE_RANGE_COST = 40;
    private static final int BASE_COOLDOWN_COST = 60;
    private static final float COST_MULTIPLIER = 2f;

    /**
     * Construtor completo que agora recebe o BrainrotTower em vez de apenas
     * um int towerID, permitindo acesso ao grupo de sinergia e efeito associado.
     */
    public Tower(Texture texture, Texture synergyTexture,Vector2 position, String displayName, int damage, int attackSpeed, float range, float attackCooldown,
            int width, int height, BrainrotTower towerType) {
        this.id = UUID.randomUUID().toString();
        this.textureRegion = texture != null ? new TextureRegion(texture) : null;
        this.synergyTexture = synergyTexture;
        this.originalTexture = texture;
        this.position = position;
        this.width = width;
        this.height = height;
        this.displayName = displayName;
        this.damage = damage;
        this.attackSpeed = attackSpeed;
        this.range = range;
        this.attackCooldown = attackCooldown;
        this.lastAttackTime = 0;
        this.towerType = towerType;
        this.towerID = towerType != null ? towerType.ordinal() : 0;
        this.damageUpgradeLevel = 0;
        this.rangeUpgradeLevel = 0;
        this.cooldownUpgradeLevel = 0;
        this.bonusDamage = 0;
        this.valueSpent = 0;
        this.description = "";
        this.activeSynergyBoosts = new ArrayList<>();
    }

    public abstract void update(float gameTime, Array<Enemy> activeEnemies, Array<Tower> activeTowers,
                                 CoinManager coinManager, ProjectileManager projectileManager, Texture projectileTexture);

    @Override
    public void draw(SpriteBatch batch) {
        float x = position.x - width / 2f;
        float y = position.y;
        batch.draw(textureRegion, x, y, width, height);
    }

    public void addValueSpent(int amount) 
    {
        this.valueSpent += amount;
    }

    public int getValueSpent() 
    {
        return valueSpent;
    }

    public void attack(float gameTime) {
        lastAttackTime = gameTime;
    }

    public float getLastAttackTime() {
        return lastAttackTime;
    }

    public float getAttackCooldown() {
        // Aplica multiplicador de cooldown das sinergias ativas
        float multiplier = 1f;
        for (SynergyBoost boost : activeSynergyBoosts) {
            multiplier *= boost.getCooldownMultiplier();
        }
        return attackCooldown * multiplier;
    }

    /**
     * Mudar para textura de sinergia
     */
    public void synergyTextureSwitch()
    {
        if (synergyTexture != null && textureRegion.getTexture() != synergyTexture) {
            textureRegion.setTexture(synergyTexture);
        }
    }

    /**
     * Restaura a textura original da torre (quando a sinergia termina).
     */
    public void resetTexture()
    {
        if (originalTexture != null && textureRegion.getTexture() != originalTexture) {
            textureRegion.setTexture(originalTexture);
        }
    }

    /**
     * Calcula o dano total incluindo:
     * - Dano base da torre (damage)
     * - Bônus de upgrades e TowerBoost (bonusDamage)
     * - Multiplicadores de sinergias ativas
     *
     * Os multiplicadores de sinergia são compostos (multiplicados entre si),
     * permitindo que múltiplas sinergias stack sem conflito.
     */
    public int getTotalDamage() {
        float base = damage + bonusDamage;
        float multiplier = 1f;
        for (SynergyBoost boost : activeSynergyBoosts) {
            multiplier *= boost.getDamageMultiplier();
        }
        return Math.round(base * multiplier);
    }

    public void addBonusDamage(int amount) {
        this.bonusDamage += amount;
    }

    public int getDamage() {
        return damage;
    }

    public int getID() {
        return towerID;
    }

    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * Retorna o alcance incluindo multiplicadores de sinergia.
     */
    public float getRange() {
        float baseRange = range;
        float multiplier = 1f;
        for (SynergyBoost boost : activeSynergyBoosts) {
            multiplier *= boost.getRangeMultiplier();
        }
        return baseRange * multiplier;
    }

    public void setPosition(Vector2 newPosition) {
        this.position = newPosition;
    }

    public String getName() {
        return this.displayName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getTowerHeight() {
        return this.height;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getDamageUpgradeLevel() { return damageUpgradeLevel; }
    public int getRangeUpgradeLevel() { return rangeUpgradeLevel; }
    public int getCooldownUpgradeLevel() { return cooldownUpgradeLevel; }

    public int getDamageUpgradeCost() {
        return (int)(BASE_DAMAGE_COST * Math.pow(COST_MULTIPLIER, damageUpgradeLevel));
    }

    public int getRangeUpgradeCost() {
        return (int)(BASE_RANGE_COST * Math.pow(COST_MULTIPLIER, rangeUpgradeLevel));
    }

    public int getCooldownUpgradeCost() {
        return (int)(BASE_COOLDOWN_COST * Math.pow(COST_MULTIPLIER, cooldownUpgradeLevel));
    }

    public void upgradeDamage() {
        this.damage += 10;
        this.damageUpgradeLevel++;
    }

    public String getDamageUpgradeLabel() {
        return "Damage +10 [Lv." + damageUpgradeLevel + "]  $" + getDamageUpgradeCost();
    }

    public String getRangeUpgradeLabel() {
        return "Range +25 [Lv." + rangeUpgradeLevel + "]  $" + getRangeUpgradeCost();
    }

    public String getCooldownUpgradeLabel() {
        return "Speed +15% [Lv." + cooldownUpgradeLevel + "]  $" + getCooldownUpgradeCost();
    }

    public void upgradeRange() {
        this.range += 25;
        this.rangeUpgradeLevel++;
    }

    public void upgradeCooldown() {
        this.attackCooldown = Math.max(0.1f, this.attackCooldown * 0.85f);
        this.cooldownUpgradeLevel++;
    }

    // ==================== EFEITOS ====================

    /**
     * Cria uma cópia do efeito de status que esta torre aplica.
     * Por padrão, delega para BrainrotTower.createEffectPrototype().
     * Torres que não causam dano (TowerCoin, TowerBoost) devem sobrescrever
     * retornando null.
     */
    public StatusEffect createEffect() {
        return towerType != null ? towerType.createEffectPrototype() : null;
    }

    // ==================== SINERGIAS ====================

    public SynergyGroup getSynergyGroup() {
        return towerType != null ? towerType.getSynergyGroup() : SynergyGroup.PHYSICAL;
    }

    /**
     * Adiciona um boost de sinergia a esta torre.
     * Chamado pelo SynergyManager quando uma sinergia é ativada.
     * Os boosts são acumulativos (multiplicativos entre si).
     */
    public void addSynergyBoost(SynergyBoost boost) {
        if (!activeSynergyBoosts.contains(boost)) {
            activeSynergyBoosts.add(boost);
        }
    }

    /**
     * Remove um boost de sinergia.
     * Chamado pelo SynergyManager quando uma sinergia é desativada.
     */
    public void removeSynergyBoost(SynergyBoost boost) {
        activeSynergyBoosts.remove(boost);
    }

    public BrainrotTower getTowerType() {
        return towerType;
    }
}
