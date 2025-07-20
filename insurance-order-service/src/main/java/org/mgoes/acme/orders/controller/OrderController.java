package org.mgoes.acme.orders.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.OrderService;
import org.mgoes.acme.orders.openapi.api.OrdersApiDelegate;
import org.mgoes.acme.orders.openapi.model.OrderCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderController implements OrdersApiDelegate {

    private final OrderService orderService;

    @Override
    public ResponseEntity<Void> createInsuranceOrder(OrderCreate orderCreate){
        log.info(orderCreate.toString());
        orderService.createOrder(orderCreate);
        return ResponseEntity.created(URI.create("http://ooo.com/35")).build();
    }
}
