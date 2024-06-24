package com.sonic.sonic_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:security.properties")
@SpringBootApplication
public class SonicBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SonicBackendApplication.class, args);
	}

}
