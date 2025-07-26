package org.mgoes.acme.orders.model;

public class IllegalOrderStateTransitionException extends RuntimeException {
    public IllegalOrderStateTransitionException(String message) {
        super(message);
    }
    public IllegalOrderStateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
