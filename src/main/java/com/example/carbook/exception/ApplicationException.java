package com.example.carbook.exception;

import java.io.Serial;

public abstract class ApplicationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 149218657957801356L;

    protected ApplicationException(String message) {
        super(message);
    }

}