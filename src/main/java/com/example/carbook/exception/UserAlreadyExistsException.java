package com.example.carbook.exception;

import java.io.Serial;

public class UserAlreadyExistsException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 6344836722863396350L;

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}