package com.jwd.cafe.exception;

public class EmailException extends Exception {
    public EmailException() {
    }

    public EmailException(final String message) {
        super(message);
    }

    public EmailException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmailException(final Throwable cause) {
        super(cause);
    }

    public EmailException(final String message,final Throwable cause,final boolean enableSuppression,final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
