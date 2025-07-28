package org.mgoes.acme.orders.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class OrderTest {

    @Test
    void givenInvalidInitialStateShouldThrowsIllegalOrderStateTransitionException(){
        var order = new Order();
        assertThrows(IllegalOrderStateTransitionException.class, () ->
            order.setState(OrderState.CANCELED)
        );
    }

    @Test
    void givenValidInitialStateShouldSetValueCorrectly(){
        var order = new Order();
        order.setState(OrderState.RECEIVED);

        assertEquals(OrderState.RECEIVED, order.getState());
        assertEquals("RECEIVED", order.getStatus());
    }

    @Test
    void givenInvalidTransitionShouldThrowsIllegalOrderStateTransitionException(){
        var order = new Order();
        order.setState(OrderState.RECEIVED);
        assertThrows(IllegalOrderStateTransitionException.class, () ->
                order.setState(OrderState.APPROVED)
        );
    }

    @Test
    void givenValidTransitionShouldSetValueCorrectly(){
        var order = new Order();
        order.setState(OrderState.RECEIVED);
        order.setState(OrderState.REJECTED);

        assertEquals(OrderState.REJECTED, order.getState());
        assertEquals("REJECTED", order.getStatus());
    }

    @Test
    void givenFreshInstanceShouldSetInitialState(){
        var order = new Order();
        order.setInitialState();

        assertEquals(OrderState.RECEIVED, order.getState());
        assertEquals("RECEIVED", order.getStatus());
    }

    @Test
    void givenInitliazedInstanceShouldThrowsIllegalOrderStateTransitionException(){
        var order = new Order();
        order.setInitialState();
        order.setState(OrderState.VALIDATED);

        assertThrows(IllegalOrderStateTransitionException.class,
                order::setInitialState
        );
    }

    @Test
    void givenFreshInstanceAndSetStatusShouldSetValueCorrectly(){
        var order = new Order();
        order.setStatus("PENDING");

        assertEquals("PENDING", order.getStatus());
        assertEquals(OrderState.PENDING, order.getState());
    }

    @Test
    void givenInitliazedInstanceAndSetStatusShouldThrowsIllegalOrderStateTransitionException(){
        var order = new Order();
        order.setInitialState();

        assertThrows(IllegalOrderStateTransitionException.class, () ->
                order.setStatus("PENDING")
        );
    }

}