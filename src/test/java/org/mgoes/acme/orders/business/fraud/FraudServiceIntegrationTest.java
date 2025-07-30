package org.mgoes.acme.orders.business.fraud;

import org.junit.jupiter.api.Test;
import org.mgoes.acme.orders.TestContainersBase;
import org.mgoes.acme.orders.business.fraud.model.FraudRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class FraudServiceIntegrationTest extends TestContainersBase {

    @Autowired
    FraudService fraudService;

    @Test
    void givenNoInformationCustomerShouldReturnNoInformationClassification(){
        var request = new FraudRequest("e053467f-bd48-4fd2-9481-75bd4e88ee40", "2e8de788-af74-4d67-a8b9-25d7efa9ff09");
        var analysis = fraudService.getAnalysis(request);

        assertNotNull(analysis.getOrderId());
        assertNotNull(analysis.getCustomerId());
        assertEquals("NO_INFORMATION", analysis.getClassification());
    }

    @Test
    void givenPreferredCustomerShouldReturnPreferredClassification(){
        var request = new FraudRequest("e053467f-bd48-4fd2-9481-75bd4e88ee40", "2d3e90fc-5672-4fe9-9d68-c292e5249815");
        var analysis = fraudService.getAnalysis(request);

        assertNotNull(analysis.getOrderId());
        assertNotNull(analysis.getCustomerId());
        assertEquals("PREFERRED", analysis.getClassification());
    }

    @Test
    void givenRegularCustomerShouldReturnRegularClassification(){
        var request = new FraudRequest("e053467f-bd48-4fd2-9481-75bd4e88ee40", "d91d586a-8240-4cdc-a821-17475340c70d");
        var analysis = fraudService.getAnalysis(request);

        assertNotNull(analysis.getOrderId());
        assertNotNull(analysis.getCustomerId());
        assertEquals("REGULAR", analysis.getClassification());
    }

    @Test
    void givenHighRiskCustomerShouldReturnHighRiskClassification(){
        var request = new FraudRequest("e053467f-bd48-4fd2-9481-75bd4e88ee40", "b8304e05-0178-4e41-81e2-403df10f9263");
        var analysis = fraudService.getAnalysis(request);

        assertNotNull(analysis.getOrderId());
        assertNotNull(analysis.getCustomerId());
        assertEquals("HIGH_RISK", analysis.getClassification());
    }

}