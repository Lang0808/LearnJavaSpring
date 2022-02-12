package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication(scanBasePackages = {"com.example.demo"})
public class DemoApplication {
	private static final Logger log= LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		log.info("Hello main");
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder){
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception{
		return args -> {
			log.info("Hello is me");
			Echo echo=restTemplate.getForObject("https://reqbin.com/echo/get/json", Echo.class);
			System.out.println(echo.toString());
			log.info(echo.toString());
		};
	}
}

// set JAVA_HOME = C:\Program Files\Java\jdk-17.0.2
// mvnw spring-boot:run
