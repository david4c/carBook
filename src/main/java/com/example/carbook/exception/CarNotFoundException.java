package com.example.carbook.exception;

import java.io.Serial;

public class CarNotFoundException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = -6310356816699208970L;

    public CarNotFoundException(String message) {
        super(message);
    }
}