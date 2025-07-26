package org.mgoes.acme.orders.business.fraud;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.mgoes.acme.orders.model.InsuranceCategory;
import org.mgoes.acme.orders.model.RiskClassification;

@AllArgsConstructor
@Data
public class RiskCategoryWrapper {
    private RiskClassification classification;
    private InsuranceCategory category;
}
