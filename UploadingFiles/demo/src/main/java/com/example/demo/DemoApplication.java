package com.example.demo;

import com.example.demo.storage.StorageProperties;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.example.demo.storage.StorageServices;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DemoApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext context=SpringApplication.run(DemoApplication.class, args);
		StorageProperties storage=context.getBean(StorageProperties.class);
		System.out.println(storage.getLocation());
	}

	@Bean
	CommandLineRunner init(StorageServices storageServices) {
		return (args) -> {
			storageServices.deleteAll();
			storageServices.init();
		};
	}
}
