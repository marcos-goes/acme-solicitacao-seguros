//package org.mgoes.acme.orders;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.testcontainers.containers.ComposeContainer;
//import org.testcontainers.containers.MockServerContainer;
//import org.testcontainers.containers.RabbitMQContainer;
//import org.testcontainers.utility.DockerImageName;
//
//import java.io.File;
//
////@SpringBootTest
//@AutoConfigureMockMvc
//class BaseTestContainers {
//
//    public ComposeContainer environment = new ComposeContainer(
//            new File("docker-compose.yaml")
//    )
//            .withExposedService("postgresql", 5432)
//            .withExposedService("mockserver", 1080)
//            .withExposedService("rabbitmq", 5672);
//
//
//
//    @Test
//    void aa(){
//        var mockServer = new MockServerContainer(DockerImageName
//                .parse("mockserver/mockserver:5.15.0"));
//        mockServer.start();
//
//        var rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:4-management"));
//        rabbit.start();
//
//        environment.start();
//
//    }
//
//}
