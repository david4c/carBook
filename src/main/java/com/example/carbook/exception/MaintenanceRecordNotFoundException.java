package com.example.carbook.exception;

import java.io.Serial;

public class MaintenanceRecordNotFoundException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = -5016097744843478876L;

    public MaintenanceRecordNotFoundException(String message) {
        super(message);
    }
}