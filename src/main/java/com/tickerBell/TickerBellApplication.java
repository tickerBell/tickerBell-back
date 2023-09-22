package com.tickerBell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TickerBellApplication {

	public static void main(String[] args) {
		SpringApplication.run(TickerBellApplication.class, args);
	}

}
