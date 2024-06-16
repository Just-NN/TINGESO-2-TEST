package com.tickets.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class BonusApplication {

	public static void main(String[] args) {
		SpringApplication.run(BonusApplication.class, args);
	}

}
