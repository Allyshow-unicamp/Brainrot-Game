package com.brainrots.exception;

/** Exceção para ações inválidas do jogo (ex: compra na loja sem saldo). */
public class InvalidGameActionException extends Exception {
    public InvalidGameActionException(String message) {
        super(message);
    }
}
