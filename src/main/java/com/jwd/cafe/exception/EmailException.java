package com.jwd.cafe.exception;

/**
 * The exception will be thrown when {@link com.jwd.cafe.mail.AbstractMailSender} will fail to send an email
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
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
