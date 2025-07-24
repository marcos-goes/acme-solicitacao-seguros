package org.mgoes.acme.orders.business.fraud.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class FraudRequest {
    private UUID orderId;
    private UUID customerId;
}
