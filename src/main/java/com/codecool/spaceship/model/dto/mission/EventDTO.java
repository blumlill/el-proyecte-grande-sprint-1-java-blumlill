package com.codecool.spaceship.model.dto.mission;

import java.time.LocalDateTime;

public record EventDTO(LocalDateTime time, String message) {
}
