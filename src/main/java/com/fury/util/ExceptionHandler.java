package com.fury.util;

/**
 * Used to delegate exception handling.
 *
 * @author Demmonic
 */
public interface ExceptionHandler {

    /**
     * Attempts to handle the provided {@link Exception}.
     *
     * @param e The Exception to handle
     */
    void handleException(Exception e);

}
