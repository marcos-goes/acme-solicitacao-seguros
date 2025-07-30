package org.mgoes.acme.orders.business.fraud;

import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.OrderLifeCycleMediator;
import org.mgoes.acme.orders.business.OrderService;
import org.mgoes.acme.orders.business.fraud.model.FraudAnalysis;
import org.mgoes.acme.orders.business.fraud.model.FraudRequest;
import org.mgoes.acme.orders.model.InsuranceCategory;
import org.mgoes.acme.orders.model.OrderState;
import org.mgoes.acme.orders.model.RiskClassification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Map;

import static org.mgoes.acme.orders.business.MediatorEvent.FRAUD_ANALISYS_ACCEPTED;
import static org.mgoes.acme.orders.business.MediatorEvent.FRAUD_ANALISYS_REJECTED;
import static org.mgoes.acme.orders.model.InsuranceCategory.*;
import static org.mgoes.acme.orders.model.RiskClassification.*;

@Slf4j
@Component
public class FraudService {

    private final RestClient restClient = RestClient.create();
    private final String fraudServiceBaseUri;
    private final OrderService orderService;

    private final Map<RiskCategoryWrapper, BigDecimal> riskMatrix = Map.ofEntries(
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(REGULAR, LIFE), BigDecimal.valueOf(500000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(REGULAR, HOME), BigDecimal.valueOf(500000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(REGULAR, AUTO), BigDecimal.valueOf(350000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(REGULAR, TRAVEL), BigDecimal.valueOf(255000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(REGULAR, HEALTH), BigDecimal.valueOf(255000)),

            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(HIGH_RISK, LIFE), BigDecimal.valueOf(125000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(HIGH_RISK, HOME), BigDecimal.valueOf(150000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(HIGH_RISK, AUTO), BigDecimal.valueOf(250000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(HIGH_RISK, TRAVEL), BigDecimal.valueOf(125000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(HIGH_RISK, HEALTH), BigDecimal.valueOf(125000)),

            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(PREFERRED, LIFE), BigDecimal.valueOf(799999.99d)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(PREFERRED, HOME), BigDecimal.valueOf(449999.99d)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(PREFERRED, AUTO), BigDecimal.valueOf(449999.99d)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(PREFERRED, TRAVEL), BigDecimal.valueOf(375000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(PREFERRED, HEALTH), BigDecimal.valueOf(375000)),

            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(NO_INFORMATION, LIFE), BigDecimal.valueOf(200000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(NO_INFORMATION, HOME), BigDecimal.valueOf(200000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(NO_INFORMATION, AUTO), BigDecimal.valueOf(75000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(NO_INFORMATION, TRAVEL), BigDecimal.valueOf(55000)),
            new AbstractMap.SimpleEntry<>(new RiskCategoryWrapper(NO_INFORMATION, HEALTH), BigDecimal.valueOf(55000))
    );

    public FraudService(@Value("${fraud.url}") String fraudServiceBaseUri, OrderService orderService){
        this.fraudServiceBaseUri = fraudServiceBaseUri;
        this.orderService = orderService;
    }

    FraudAnalysis getAnalysis(FraudRequest request){
        return restClient.post()
                .uri(fraudServiceBaseUri + "/v1/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(FraudAnalysis.class);
    }

    @Async
    public void executeRiskAnalysis(String orderId, OrderLifeCycleMediator mediator) {

        var order = orderService.getOrderById(orderId).get();

        log.info("Requesting Fraud Service. Order {}, Customer = {}", order.getId(), order.getCustomerId());
        var result = getAnalysis(new FraudRequest(order.getId(), order.getCustomerId()));

        var risk = RiskClassification.valueOf(result.getClassification());
        var category = order.getCategory();

        order.setClassification(risk);

        var event = FRAUD_ANALISYS_REJECTED;
        var nextState = OrderState.REJECTED;

        if(isAcceptableRisk(risk, category, order.getInsuredAmount())) {
            event = FRAUD_ANALISYS_ACCEPTED;
            nextState = OrderState.VALIDATED;
        } else {
            order.setFinishedAt(LocalDateTime.now());
        }

        order.setState(nextState);
        orderService.saveOrder(order);
        mediator.notify(event, order.getId());
    }

    boolean isAcceptableRisk(RiskClassification classification, InsuranceCategory category, BigDecimal insuredAmount){
        return insuredAmount.compareTo(
                riskMatrix.getOrDefault(new RiskCategoryWrapper(classification, category), BigDecimal.valueOf(0)))
                <= 0;
    }
}
