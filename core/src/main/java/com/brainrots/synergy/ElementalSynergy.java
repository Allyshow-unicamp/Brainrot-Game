package com.brainrots.synergy;

import com.badlogic.gdx.utils.Array;
import com.brainrots.tower.Tower;

/**
 * Sinergia baseada em contagem de torres do mesmo grupo elemental.
 *
 * Quando o número de torres de SynergyGroup X no campo atinge ou
 * ultrapassa o limiar (threshold), a sinergia é ativada e todas as
 * torres daquele grupo recebem o boost configurado.
 *
 * Esta classe é reutilizável para qualquer grupo — basta instanciar
 * com parâmetros diferentes. Exemplo de uso no SynergyManager:
 *
 *   addRule(new ElementalSynergy(SynergyGroup.FIRE, 3, new SynergyBoost(1.15f)));
 *   addRule(new ElementalSynergy(SynergyGroup.SUPPORT, 3, new SynergyBoost(1.0f, 2.0f)));
 *
 * Isso elimina a necessidade de classes redundantes como FireSynergy,
 * IceSynergy, etc., mantendo a flexibilidade via composição.
 */
public class ElementalSynergy implements SynergyRule {

    private SynergyGroup group;
    private int threshold;
    private SynergyBoost boost;

    /**
     * @param group     grupo elemental que esta regra fiscaliza
     * @param threshold número mínimo de torres do grupo para ativar
     * @param boost     boost concedido quando a sinergia está ativa
     */
    public ElementalSynergy(SynergyGroup group, int threshold, SynergyBoost boost) {
        this.group = group;
        this.threshold = threshold;
        this.boost = boost;
    }

    /**
     * Verifica se a quantidade de torres do grupo atinge o threshold.
     * Percorre toda a lista de torres e conta as que pertencem a este grupo.
     */
    @Override
    public boolean isActivated(Array<Tower> towers) {
        int count = 0;
        for (Tower tower : towers) {
            if (tower.getSynergyGroup() == group) {
                count++;
                if (count >= threshold) {
                    return true; // early exit: já atingiu o mínimo
                }
            }
        }
        return false;
    }

    /**
     * Aplica o boost apenas às torres que pertencem a este grupo.
     * Torres de outros grupos não são afetadas.
     */
    @Override
    public void apply(Tower tower) {
        if (tower.getSynergyGroup() == group) {
            tower.addSynergyBoost(boost);
            tower.synergyTextureSwitch();
        }
    }

    /**
     * Remove o boost apenas das torres deste grupo.
     */
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
