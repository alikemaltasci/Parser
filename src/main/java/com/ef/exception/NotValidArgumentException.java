package com.ef.exception;

public class NotValidArgumentException extends Exception {

    public NotValidArgumentException(final String errorMessage) {
        super(errorMessage);
    }
}
