package com.customorm.exception;

/**
 * Thrown when an entity is not found in database
 */
public class EntityNotFoundException extends ORMException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}