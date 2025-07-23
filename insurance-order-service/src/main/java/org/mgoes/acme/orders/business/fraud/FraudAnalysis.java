package org.mgoes.acme.orders.business.fraud;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class FraudAnalysis {
    private UUID orderId;
    private UUID customerId;
    private LocalDateTime analyzedAt;
    private FraudClassification classification;
    private List<FraudOccurrency> occurrences = new ArrayList<>();
}
