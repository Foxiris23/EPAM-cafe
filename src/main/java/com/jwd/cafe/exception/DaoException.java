package com.jwd.cafe.exception;

/**
 * The exception will be thrown when execution of sql query or parsing {@link java.sql.ResultSet} will fail
 * @author Mark Kazyrytski on 2021-01-31.
 * @version 1.0.0
 */
public class DaoException extends Exception {
    public DaoException() {
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
