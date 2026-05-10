package com.example.calorieCounter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"spring.kafka.listener.auto-startup=false"})
	@ActiveProfiles("test")
class CalorieCounterApplicationTests {

	@Test
	void contextLoads() {
	}

}
