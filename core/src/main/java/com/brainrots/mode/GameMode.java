package com.brainrots.mode;

import com.badlogic.gdx.utils.Array;
import com.brainrots.engine.Base;
import com.brainrots.engine.LevelManager;
import com.brainrots.wave.WaveManager;
import com.brainrots.enemy.Enemy;

/**
 * Interface que define o modo de jogo atual (State Pattern).
 *
 * Cada implementação encapsula completamente as regras de:
 * - Condição de vitória (evaluate retorna VICTORY)
 * - Condição de derrota (evaluate retorna DEFEAT)
 * - Comportamento ao ativar o modo (onEnter)
 * - Texto exibido no HUD (getHudSuffix)
 *
 * Vantagens sobre flags booleanas e condicionais:
 * - As regras de cada modo estão isoladas em sua própria classe
 * - Novo modo = nova classe, sem modificar LevelManager (OCP)
 * - LevelManager delega para GameMode sem saber detalhes internos
 * - Testável isoladamente: cada modo tem sua própria lógica de vitória
 */
public interface GameMode {

    /**
     * Avalia o estado atual do jogo e retorna o resultado.
     * Chamado a cada frame no LevelManager.update().
     *
     * @param context     referência ao LevelManager para acesso a estado
     * @param base        base do jogador (HP)
     * @param waveManager gerenciador de waves (wave atual, max waves)
     * @param enemies     lista de inimigos ativos
     * @return VICTORY, DEFEAT ou NONE
     */
    GameOutcome evaluate(LevelManager context, Base base, WaveManager waveManager, Array<Enemy> enemies);

    /** Chamado quando o modo é ativado (ex: para configurar estado inicial). */
    void onEnter(LevelManager context);

    /**
     * Sufixo exibido no HUD ao lado do número da wave.
     */
    String getHudSuffix();
}
