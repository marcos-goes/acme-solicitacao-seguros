package org.mgoes.acme.orders.model.state;

import org.mgoes.acme.orders.model.OrderStatus;

import java.util.Set;

public interface OrderState {
    boolean mayTransitionTo(OrderStatus newState);

    Set<OrderStatus> getAllowedTransitions();
}
