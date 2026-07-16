package com.brainrots.synergy;

import com.badlogic.gdx.utils.Array;
import com.brainrots.tower.Tower;

/**
 * Sinergia por contagem de torres de um grupo elemental.
 * Ativa quando o número de torres do grupo atinge o threshold.
 */
public class ElementalSynergy implements SynergyRule {

    private SynergyGroup group;
    private int threshold;
    private SynergyBoost boost;

    /**
     * @param group     grupo elemental monitorado
     * @param threshold número mínimo de torres para ativar
     * @param boost     boost aplicado quando ativa
     */
    public ElementalSynergy(SynergyGroup group, int threshold, SynergyBoost boost) {
        this.group = group;
        this.threshold = threshold;
        this.boost = boost;
    }

    @Override
    public boolean isActivated(Array<Tower> towers) {
        int count = 0;
        for (Tower tower : towers) {
            if (tower.getSynergyGroup() == group) {
                count++;
                if (count >= threshold) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void apply(Tower tower) {
        if (tower.getSynergyGroup() == group) {
            tower.addSynergyBoost(boost);
            tower.synergyTextureSwitch();
        }
    }

    @Override
    public void remove(Tower tower) {
        if (tower.getSynergyGroup() == group) {
            tower.removeSynergyBoost(boost);
            tower.resetTexture();
        }
    }

    @Override
    public String getName() {
        return group.name() + " Synergy (" + threshold + "+)";
    }

    @Override
    public SynergyGroup getGroup() {
        return group;
    }
}
