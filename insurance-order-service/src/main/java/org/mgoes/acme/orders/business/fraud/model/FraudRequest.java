package org.mgoes.acme.orders.business.fraud.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FraudRequest {
    private String orderId;
    private String customerId;
}
