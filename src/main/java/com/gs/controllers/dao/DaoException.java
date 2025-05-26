package com.gs.controllers.dao;

/**
 * Exception thrown by DAO methods when any data-access error occurs.
 * Wrapps the underlying SQLException or other persistence framework exception.
 */
public class DaoException extends RuntimeException {
    /**
     * Constructs a new DaoException with the specified detail message and cause.
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new DaoException with the specified cause.
     */
    public DaoException(Throwable cause) {
        super(cause);
    }

    public DaoException(String message) {
        super(message);
    }
}
