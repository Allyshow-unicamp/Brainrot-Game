package com.brainrots.engine.mode;

/**
 * Resultado da avaliação do GameMode a cada frame.
 *
 * NONE:  jogo continua normalmente
 * VICTORY: jogador venceu (trigga tela de vitória)
 * DEFEAT: base foi destruída (trigga tela de derrota)
 *
 * Esta enumeração separa a avaliação (o que aconteceu) da reação
 * (o que fazer), permitindo que LevelManager trate qualquer GameMode
 * sem saber o que cada modo considera vitória ou derrota.
 */
public enum GameOutcome {
    NONE,
    VICTORY,
    DEFEAT
}
