package com.jwd.cafe.exception;

/**
 * The exception will be thrown when entity from database is not represented in java
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
