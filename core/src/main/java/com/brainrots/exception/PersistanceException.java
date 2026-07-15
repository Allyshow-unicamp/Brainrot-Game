package com.brainrots.exception;

/** Exceção para falhas de persistência (leitura/escrita de JSON). */
public class PersistanceException extends Exception {
    public PersistanceException(String message) {
        super(message);
    }
}
