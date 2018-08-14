package com.revature.caliber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@EnableAutoConfiguration
@EnableEurekaClient
@ComponentScan(basePackages = {"com.revature"})
@EnableAspectJAutoProxy
@ImportResource("classpath:spring-security.xml")
public class Application {
	private String test = "Test";
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	public String getTest() {
		return test;
	}
	
}
