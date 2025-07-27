package org.mgoes.acme.orders.business;

import lombok.RequiredArgsConstructor;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.model.OrderState;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.mgoes.acme.orders.business.MediatorEvent.*;

@RequiredArgsConstructor
@Component
public class ApproveOrderService {

    private final OrderService orderService;

    public void approve(String orderId, OrderLifeCycleMediator mediator) {
        var order = orderService.getOrderById(orderId).get();

        if(order.getHistory().stream()
                .map(HistoryItem::getAdditionalInfo)
                .collect(Collectors.toSet())
                .containsAll(List.of(INSURANCE_SUBSCRITION_CONFIRMED.name(), PAYMENT_CONFIRMED.name()))){

            order.setState(OrderState.APPROVED);
            order.setFinishedAt(LocalDateTime.now());
            orderService.saveOrder(order);

            mediator.notify(APPROVE_ORDER, orderId);
        }
    }
}
