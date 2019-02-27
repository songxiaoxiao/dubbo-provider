package com.winter.dubbo.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

@SpringBootApplication

public class ProviderApplication {
	private final static Logger logger = LoggerFactory.getLogger(ProviderApplication.class);

	public static void main(String[] args)throws IOException {
//		SpringApplication.run(ProviderApplication.class, args);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:provider.xml");
		context.start();
		logger.info("dubbo provider application started...");
		System.in.read();
	}


}
