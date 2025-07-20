package org.mgoes.acme.orders.controller;

import lombok.RequiredArgsConstructor;
import org.mgoes.acme.orders.business.OrderService;
import org.mgoes.acme.orders.mapper.OrderMapper;
import org.mgoes.acme.orders.openapi.api.OrdersApiDelegate;
import org.mgoes.acme.orders.openapi.model.Order;
import org.mgoes.acme.orders.openapi.model.OrderCreate;
import org.mgoes.acme.orders.openapi.model.OrderCreateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderController implements OrdersApiDelegate {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderCreateResponse> createInsuranceOrder(OrderCreate orderCreate){
        var order = OrderMapper.INSTANCE.toOrderEntity(orderCreate);
        order = orderService.createOrder(order);
        var response = OrderMapper.INSTANCE.toOrderCreateResponse(order);

        return ResponseEntity
                .created(URI.create("api/vi/orders/" + response.getId().toString()))
                .body(response);
    }

    @Override
    public ResponseEntity<Order> getOrder(UUID id) {
        var optOrder = orderService.getOrderById(id);

        if(optOrder.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity
                .ok(OrderMapper.INSTANCE.toApiOrder(optOrder.get()));
    }
}
