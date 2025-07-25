package org.mgoes.acme.orders.model.state;

import org.mgoes.acme.orders.model.OrderStatus;

import java.util.Set;

public class Pending  implements OrderState {



    @Override
    public boolean mayTransitionTo(OrderStatus newState) {
        return false;
    }

    @Override
    public Set<OrderStatus> getAllowedTransitions() {
        return Set.of();
    }
}
