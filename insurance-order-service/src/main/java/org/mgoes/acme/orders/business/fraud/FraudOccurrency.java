package org.mgoes.acme.orders.business.fraud;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class FraudOccurrency {
    private UUID id;
    private Long productId;
    private String type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
