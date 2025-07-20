package org.mgoes.acme.orders.business;

import lombok.RequiredArgsConstructor;
import org.mgoes.acme.orders.mapper.OrderMapper;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.openapi.model.OrderCreate;
import org.mgoes.acme.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public void createOrder(OrderCreate dto){
        var initialStatus = "RECEIVED";
        var now = LocalDateTime.now();

        var historyItem = new HistoryItem();
        historyItem.setStatus(initialStatus);
        historyItem.setRegistered(now);

        var entity = orderMapper.toOrderEntity(dto);
        entity.setId(UUID.randomUUID());
        entity.setStatus(initialStatus);
        entity.setCreated(now);
        entity.setHistory(List.of(historyItem));

        orderRepository.save(entity);
    }
}
