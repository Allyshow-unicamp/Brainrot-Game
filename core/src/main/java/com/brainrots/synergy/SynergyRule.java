package com.brainrots.synergy;

import com.badlogic.gdx.utils.Array;
import com.brainrots.tower.Tower;

/** Interface (Strategy) que define uma regra de sinergia. */
public interface SynergyRule {

    /** Verifica se a condição de ativação é satisfeita pelas torres atuais. */
    boolean isActivated(Array<Tower> towers);

    /** Aplica o boost da sinergia a uma torre (chamado na transição inativo → ativo). */
    void apply(Tower tower);

    /** Remove o boost da sinergia de uma torre (chamado na transição ativo → inativo). */
    void remove(Tower tower);

    /** Nome legível para exibição na UI. */
    String getName();

    /** Grupo associado a esta sinergia. */
    SynergyGroup getGroup();
}
