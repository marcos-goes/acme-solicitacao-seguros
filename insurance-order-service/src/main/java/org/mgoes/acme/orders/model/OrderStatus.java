package org.mgoes.acme.orders.model;

import lombok.RequiredArgsConstructor;
import org.mgoes.acme.orders.model.state.OrderState;
import org.mgoes.acme.orders.model.state.Received;

@RequiredArgsConstructor
public enum OrderStatus {
    RECEIVED(new Received()),
    VALIDATED,
    PENDING,
    REJECTED,
    APPROVED,
    CANCELED;

    private final OrderState state;

    public boolean mayTransitionTo(OrderStatus newState){
        return state.mayTransitionTo(newState);
    }
}
