package com.customorm.exception;

/**
 * Base exception for ORM framework
 */
public class ORMException extends RuntimeException {
    public ORMException(String message) {
        super(message);
    }

    public ORMException(String message, Throwable cause) {
        super(message, cause);
    }
}