package com.nexters.buyornot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BuyornotApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuyornotApplication.class, args);
	}

}
