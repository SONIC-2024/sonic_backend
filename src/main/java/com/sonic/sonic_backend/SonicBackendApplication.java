package com.sonic.sonic_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@PropertySource("classpath:security.properties")
@EnableJpaAuditing
@SpringBootApplication
public class SonicBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(SonicBackendApplication.class, args);
	}
}
