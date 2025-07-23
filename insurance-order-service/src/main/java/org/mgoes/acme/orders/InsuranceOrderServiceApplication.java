package org.mgoes.acme.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@SpringBootApplication
public class InsuranceOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceOrderServiceApplication.class, args);
	}

	@Bean
	public ExecutorService executorService(){
		return Executors.newFixedThreadPool(10);
	}
}
