package org.mgoes.acme.orders.business.fraud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mgoes.acme.orders.business.OrderService;
import org.mgoes.acme.orders.model.InsuranceCategory;
import org.mgoes.acme.orders.model.RiskClassification;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FraudServiceTest {

    FraudService fraudService;
    OrderService orderServiceMock;

    @BeforeEach
    void setup(){
        orderServiceMock = mock(OrderService.class);
        fraudService = new FraudService("", orderServiceMock);
    }

    @ParameterizedTest
    @CsvSource({
            "REGULAR,LIFE,499999.99,true",
            "REGULAR,HOME,500000,true",
            "REGULAR,HOME,500000.01,false",
            "PREFERRED,LIFE,799999.99,true",
            "PREFERRED,LIFE,500000,true",
            "PREFERRED,LIFE,800000,false"
    })
    void shouldVerifyAcceptableRiskUsingRiskMatrix(String classification, String category, BigDecimal insuredAmount, boolean expected){
        var riskClassification = RiskClassification.valueOf(classification);
        var insuranceCategory = InsuranceCategory.valueOf(category);
        assertEquals(expected, fraudService.isAcceptableRisk(riskClassification, insuranceCategory, insuredAmount));
    }

}