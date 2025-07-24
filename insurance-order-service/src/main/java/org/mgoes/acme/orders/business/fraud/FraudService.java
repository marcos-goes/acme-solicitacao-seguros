package org.mgoes.acme.orders.business.fraud;

import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.fraud.model.FraudAnalysis;
import org.mgoes.acme.orders.business.fraud.model.FraudRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Slf4j
@Service
public class FraudService {

    private final RestClient restClient = RestClient.create();
    private final String fraudServiceBaseUri;

    public FraudService(@Value("${fraud.url}") String fraudServiceBaseUri){
        this.fraudServiceBaseUri = fraudServiceBaseUri;
    }

    private FraudAnalysis getAnalysis(FraudRequest request){
        return restClient.post()
                .uri(fraudServiceBaseUri + "/v1/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(FraudAnalysis.class);
    }

    @Async
    public void executeAnalysis(UUID orderId, UUID customerId) {
        log.info("Starting fraud analysis - Order: {}", orderId.toString());
        var result = getAnalysis(new FraudRequest(orderId, customerId));
        log.info("Fraud analysis result: {}", result);
    }
}
