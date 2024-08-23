package com.example.carbook.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = Controller.class)
public class ControllerExceptionHandler {

    private static final String MESSAGE_ATTRIBUTE = "message";
    private static final String CODE_ATTRIBUTE = "code";
    private static final String STACK_TRACE_ATTRIBUTE = "stackTrace";

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, Object> handleException(final Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put(STACK_TRACE_ATTRIBUTE, ex.getStackTrace());
        return errorResponse;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, Object> handleRuntimeException(final RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put(STACK_TRACE_ATTRIBUTE, ex.getStackTrace());
        return errorResponse;
    }

    @ExceptionHandler(MaintenanceRecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleMaintenanceRecordNotFoundException(MaintenanceRecordNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.NOT_FOUND.value());
        return errorResponse;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleUserNotFoundException(final UserNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.NOT_FOUND.value());
        return errorResponse;
    }

    @ExceptionHandler(CarNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleCarNotFoundException(final CarNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.NOT_FOUND.value());
        return errorResponse;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Map<String, Object> handleUsernameNotFoundException(final UsernameNotFoundException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.NOT_FOUND.value());
        return errorResponse;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public Map<String, Object> handleAccessDeniedException(final AccessDeniedException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.FORBIDDEN.value());
        return errorResponse;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ignoredEx) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, "Duplicate key value violates unique constraint.");
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.CONFLICT.value());
        return errorResponse;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Map<String, Object> handleUserAlreadyExistsException(final UserAlreadyExistsException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(MESSAGE_ATTRIBUTE, ex.getMessage());
        errorResponse.put(CODE_ATTRIBUTE, HttpStatus.CONFLICT.value());
        return errorResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        errors.put(CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST.value());
        return errors;
    }
}

