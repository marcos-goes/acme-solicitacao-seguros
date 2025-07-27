package org.mgoes.acme.orders.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStateTest {

    @ParameterizedTest
    @EnumSource(value = OrderState.class, names = {"VALIDATED", "CANCELED", "REJECTED"})
    void shouldValidateReceivedTransitions(OrderState transition){
        assertTrue(OrderState.RECEIVED.mayTransitionTo(transition));
    }

    @ParameterizedTest
    @EnumSource(value = OrderState.class, names = {"RECEIVED", "PENDING", "APPROVED"})
    void shouldNotValidateReceivedTransitions(OrderState transition){
        assertFalse(OrderState.RECEIVED.mayTransitionTo(transition));
    }


    @ParameterizedTest
    @EnumSource(value = OrderState.class, names = {"PENDING", "CANCELED"})
    void shouldValidateValidatedTransitions(OrderState transition){
        assertTrue(OrderState.VALIDATED.mayTransitionTo(transition));
    }

    @ParameterizedTest
    @EnumSource(value = OrderState.class, names = {"RECEIVED", "REJECTED", "APPROVED", "VALIDATED"})
    void shouldNotValidateValidatedTransitions(OrderState transition){
        assertFalse(OrderState.VALIDATED.mayTransitionTo(transition));
    }



    @ParameterizedTest
    @EnumSource(value = OrderState.class, names = {"APPROVED", "REJECTED", "PENDING", "CANCELED"})
    void shouldValidatePendingTransitions(OrderState transition){
        assertTrue(OrderState.PENDING.mayTransitionTo(transition));
    }

    @ParameterizedTest
    @EnumSource(value = OrderState.class, names = {"RECEIVED", "VALIDATED"})
    void shouldNotValidatePendingTransitions(OrderState transition){
        assertFalse(OrderState.PENDING.mayTransitionTo(transition));
    }



    @ParameterizedTest
    @EnumSource(value = OrderState.class)
    void shouldNotValidateRejectedTransitions(OrderState transition){
        assertFalse(OrderState.REJECTED.mayTransitionTo(transition));
    }

    @ParameterizedTest
    @EnumSource(value = OrderState.class)
    void shouldNotValidateApprovedTransitions(OrderState transition){
        assertFalse(OrderState.APPROVED.mayTransitionTo(transition));
    }

    @ParameterizedTest
    @EnumSource(value = OrderState.class)
    void shouldNotValidateCanceledTransitions(OrderState transition){
        assertFalse(OrderState.CANCELED.mayTransitionTo(transition));
    }
}