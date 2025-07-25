package org.mgoes.acme.orders.model.state;

import org.mgoes.acme.orders.model.OrderStatus;

import java.util.Set;

public class Received implements OrderState {

    private static final Set<OrderStatus> ALLOWED_TRANSITIONS = Set.of()

    @Override
    public boolean mayTransitionTo(OrderStatus newState) {
        return false;
    }

    @Override
    public Set<OrderStatus> getAllowedTransitions() {
        return Set.of();
    }
}
