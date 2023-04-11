package com.codecool.spaceship;

import com.codecool.spaceship.service.Initializer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpaceshipApplication {

    private final Initializer initializer;

    @Autowired
    public SpaceshipApplication(Initializer initializer) {
        this.initializer = initializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpaceshipApplication.class, args);
    }

    @PostConstruct
    public void initialize() {
        initializer.initialize();
    }
}
