package org.mgoes.acme.orders.model;

import java.util.ArrayList;
import java.util.List;

public enum OrderState {
    RECEIVED {
        @Override
        public List<OrderState> getAllowedTransitions() {
            return List.of(VALIDATED, CANCELED, REJECTED);
        }
    },
    VALIDATED {
        @Override
        public List<OrderState> getAllowedTransitions() {
            return List.of(PENDING, CANCELED);
        }
    },
    PENDING {
        @Override
        public List<OrderState> getAllowedTransitions() {
            return List.of(APPROVED, REJECTED, CANCELED, PENDING);
        }
    },
    REJECTED,
    APPROVED,
    CANCELED;

    public boolean mayTransitionTo(OrderState newState){
        return getAllowedTransitions().contains(newState);
    }

    List<OrderState> getAllowedTransitions() {
        return new ArrayList<>();
    }
}
