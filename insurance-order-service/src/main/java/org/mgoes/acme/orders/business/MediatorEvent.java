package org.mgoes.acme.orders.business;

public enum MediatorEvent {
    CREATE_ORDER,
    CANCEL_ORDER,

    FRAUD_ANALISYS_ACCEPTED,
    FRAUD_ANALISYS_REJECTED;
}
