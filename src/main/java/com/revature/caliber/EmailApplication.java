package com.revature.caliber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:spring-security.xml")
public class EmailApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}
	
}
