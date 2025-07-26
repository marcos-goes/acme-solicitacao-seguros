package org.mgoes.acme.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@SpringBootApplication
public class InsuranceOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsuranceOrderServiceApplication.class, args);
	}

	@Bean
	public Executor threadExecutor(){
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("Async-");
		executor.initialize();
		return executor;
	}

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper()
				.findAndRegisterModules()
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
}
