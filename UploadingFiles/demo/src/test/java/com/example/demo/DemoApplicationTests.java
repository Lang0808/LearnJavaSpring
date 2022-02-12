package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class DemoApplicationTests {
	@Autowired
	UploadFilesController filesController;

	@Test
	void contextLoads() {
		assertThat(filesController).isNotNull();
	}

}
