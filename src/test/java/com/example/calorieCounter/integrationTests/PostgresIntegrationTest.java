package com.example.calorieCounter.integrationTests;

import com.example.calorieCounter.entity.DailyCalories;
import com.example.calorieCounter.repository.CalorieRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(properties = {
        "spring.task.scheduling.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Testcontainers
@DirtiesContext
public class PostgresIntegrationTest {

    @Autowired
    private CalorieRepo calorieRepo;

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void shouldSaveAndReadFromPostgres() {

        DailyCalories dc = DailyCalories.builder()
                .userId(1L)
                .date(LocalDate.now())
                .consumedCalories(100.0)
                .consumedProteins(10.0)
                .consumedFats(5.0)
                .consumedCarbs(20.0)
                .carbsNorm(0.0)
                .proteinNorm(0.0)
                .fatNorm(0.0)
                .createdAt(LocalDate.now())
                .build();

        calorieRepo.save(dc);
        List<DailyCalories> all = calorieRepo.findAll();
        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
        assertEquals(100.0, all.get(0).getConsumedCalories());
    }
}
