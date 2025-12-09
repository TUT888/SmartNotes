package com.be08.smart_notes.controller;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class Base {
    @Container
    public static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.36")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init-db.sql")
            .withReuse(true);

    @Container
    public static final GenericContainer<?> redisContainer = new GenericContainer<>(DockerImageName.parse("redis:6-alpine"))
            .withExposedPorts(6379)
            .withReuse(true);

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        // MySQL configuration
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

        // JPA configuration
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQLDialect");
        registry.add("spring.jpa.show-sql", () -> "false");

        // Redis configuration
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
    }
}
