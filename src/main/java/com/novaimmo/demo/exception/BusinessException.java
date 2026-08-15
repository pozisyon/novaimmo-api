package com.novaimmo.demo.exception;

public class BusinessException
        extends RuntimeException {

    public BusinessException(
            String message
    ) {
        super(message);
    }
}