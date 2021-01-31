package com.jwd.cafe.exception;

/**
 * The exception will be thrown when application on start up
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class ApplicationStartException extends RuntimeException {
    public ApplicationStartException(String message) {
        super(message);
    }
}
