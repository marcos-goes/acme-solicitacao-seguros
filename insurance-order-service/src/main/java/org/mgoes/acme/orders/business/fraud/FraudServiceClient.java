package org.mgoes.acme.orders.business.fraud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class FraudServiceClient {

    private final RestClient restClient = RestClient.create();
    private final String fraudServiceBaseUri;

    public FraudServiceClient(@Value("${fraud.url}") String fraudServiceBaseUri){
        this.fraudServiceBaseUri = fraudServiceBaseUri;
    }

    public FraudAnalysis getAnalysis(FraudRequest request){
        return restClient.post()
                .uri(fraudServiceBaseUri + "/v1/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(FraudAnalysis.class);
    }

    public FraudAnalysis getAnalysis(UUID orderId, UUID customerId){
        return getAnalysis(new FraudRequest(orderId, customerId));
    }
}
