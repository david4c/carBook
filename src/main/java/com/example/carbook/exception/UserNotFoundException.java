package com.example.carbook.exception;

import java.io.Serial;

public class UserNotFoundException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = -1948196831530002079L;

    public UserNotFoundException(String message) {
        super(message);
    }
}