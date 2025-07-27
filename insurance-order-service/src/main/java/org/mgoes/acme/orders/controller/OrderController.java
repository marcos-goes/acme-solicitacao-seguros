package org.mgoes.acme.orders.controller;

import lombok.RequiredArgsConstructor;
import org.mgoes.acme.orders.business.OrderService;
import org.mgoes.acme.orders.mapper.OrderMapper;
import org.mgoes.acme.orders.openapi.api.OrdersApiDelegate;
import org.mgoes.acme.orders.openapi.model.OrderCreateRequest;
import org.mgoes.acme.orders.openapi.model.OrderCreateResponse;
import org.mgoes.acme.orders.openapi.model.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderController implements OrdersApiDelegate {

    private final OrderService orderService;

    @Override
    public ResponseEntity<OrderCreateResponse> createInsuranceOrder(OrderCreateRequest orderCreate){
        var order = OrderMapper.INSTANCE.toOrderEntity(orderCreate);
        order = orderService.createOrder(order);
        var response = OrderMapper.INSTANCE.toOrderCreateResponse(order);

        return ResponseEntity
                .created(URI.create("api/vi/orders/" + response.getId().toString()))
                .body(response);
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(UUID id) {
        var optOrder = orderService.getOrderById(id.toString());

        if(optOrder.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity
                .ok(OrderMapper.INSTANCE.toApiOrder(optOrder.get()));
    }

    @Override
    public ResponseEntity<List<OrderResponse>> getOrders(UUID customerId) {

        var orderList =
                Objects.isNull(customerId) ?
                        orderService.findOrders() :
                        orderService.findOrdersByCustomerId(customerId.toString());

        if(orderList.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity
                .ok(orderList
                        .stream()
                        .map(OrderMapper.INSTANCE::toApiOrder).toList());
    }

    @Override
    public ResponseEntity<Void> cancelOrder(UUID id) {

        if(!orderService.cancelOrder(id.toString()))
            return ResponseEntity.unprocessableEntity().build();

        return ResponseEntity.noContent().build();
    }
}
