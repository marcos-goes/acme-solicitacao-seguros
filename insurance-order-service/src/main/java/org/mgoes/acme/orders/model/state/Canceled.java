package org.mgoes.acme.orders.model.state;

import org.mgoes.acme.orders.model.OrderStatus;

import java.util.HashSet;
import java.util.Set;

public class Canceled implements OrderState {


    private static final Set<OrderState> ALLOWED_TRANSITIONS = new HashSet<>();

    @Override
    public boolean mayTransitionTo(OrderStatus newState) {
        return false;
    }

    @Override
    public Set<OrderStatus> getAllowedTransitions() {
        return Set.of();
    }
}
