package com.revature.caliber;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import com.splunk.Args;
import com.splunk.Receiver;
import com.splunk.Service;


@SpringBootApplication
@ImportResource("classpath:spring-security.xml")
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
		final Logger logger = Logger.getLogger(Application.class);
		logger.info("Hello world");
		
		Map<String, Object> connectionArgs = new HashMap<String, Object>();
		connectionArgs.put("host", "yourmom");
		connectionArgs.put("username", "najibismail95");
		connectionArgs.put("password", "Nj_6149746525");
		connectionArgs.put("port", "8088");
		connectionArgs.put("scheme", "https");
		
		Service splunkService = Service.connect(connectionArgs);
		Receiver receiver = splunkService.getReceiver();
		
		Args logArgs = new Args();
		logArgs.put("sourcetype", "hellosplunk");
		
		receiver.log("main", logArgs, "HelloSplunk Event");
		
		
		
	}
	
}
