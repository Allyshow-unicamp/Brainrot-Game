package com.brainrots.synergy;

import com.badlogic.gdx.utils.Array;
import com.brainrots.tower.Tower;

/**
 * Interface que define uma regra de sinergia (padrão Strategy).
 *
 * Cada implementação encapsula completamente:
 * - A condição de ativação (isActivated): quando a sinergia deve ser ligada
 * - O boost aplicado às torres quando ativa (apply)
 * - A remoção do boost quando desativa (remove)
 *
 * Vantagem sobre uma abordagem procedural com switch/if:
 * - Novas sinergias são adicionadas criando novas classes, não modificando
 *   condicionais existentes (OCP)
 * - Cada regra é testável isoladamente
 * - O SynergyManager trata todas as regras polimorficamente
 */
public interface SynergyRule {

    /**
     * Verifica se a condição para esta sinergia é satisfeita
     * pela configuração atual de torres.
     *
     * @param towers lista de todas as torres ativas no campo
     * @return true se a sinergia deve estar ativa
     */
    boolean isActivated(Array<Tower> towers);

    /**
     * Aplica o boost da sinergia a uma torre.
     * Chamado apenas na transição inativo → ativo.
     *
     * @param tower torre que deve receber o boost
     */
    void apply(Tower tower);

    /**
     * Remove o boost da sinergia de uma torre.
     * Chamado apenas na transição ativo → inativo.
     *
     * @param tower torre que deve perder o boost
     */
    void remove(Tower tower);

    /** Nome legível para exibição na UI (ex: "Fire Synergy (3+)"). */
    String getName();

    /** Grupo associado a esta sinergia. */
    SynergyGroup getGroup();
}
