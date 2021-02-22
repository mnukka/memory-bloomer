package com.github.mnukka.memory_bloomer.domain.structure.exception;

public class UnexpectedIOException extends RuntimeException {
    public UnexpectedIOException(String errorMessage) {
        super(errorMessage);
    }
}