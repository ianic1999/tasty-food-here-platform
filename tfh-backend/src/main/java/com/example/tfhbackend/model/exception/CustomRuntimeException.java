package com.example.tfhbackend.model.exception;

public class CustomRuntimeException extends RuntimeException {
    public CustomRuntimeException() {
        super();
    }

    public CustomRuntimeException(String message) {
        super(message);
    }
}
