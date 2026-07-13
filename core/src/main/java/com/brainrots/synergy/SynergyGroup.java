package com.brainrots.synergy;

/**
 * Agrupa torres por afinidade elemental/temática para fins de sinergia.
 *
 * Cada torre definida em BrainrotTower pertence a exatamente um grupo.
 * Sinergias são ativadas quando 3 ou mais torres do mesmo grupo estão
 * posicionadas no campo de batalha.
 *
 * Esta enumeração permite adicionar novos grupos sem modificar o sistema
 * de sinergia existente (aberto para extensão, fechado para modificação).
 */
public enum SynergyGroup {
    FIRE,
    ICE,
    PHYSICAL,
    SUPPORT
}
