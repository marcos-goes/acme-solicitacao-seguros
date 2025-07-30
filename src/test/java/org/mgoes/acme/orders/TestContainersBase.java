package org.mgoes.acme.orders;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.ComposeContainer;

import java.io.File;

public class TestContainersBase {

    public static ComposeContainer environment = new ComposeContainer(
            new File("src/test/resources/docker-compose-test/docker-compose.yaml")
    )
            .withLocalCompose(true)
            .withExposedService("postgresql", 5432)
            .withExposedService("mockserver", 1080)
            .withExposedService("rabbitmq", 5672);

    @BeforeAll
    static void startContainers(){
        environment.start();
    }
}
