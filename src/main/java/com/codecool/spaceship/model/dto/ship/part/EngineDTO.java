package com.codecool.spaceship.model.dto.ship.part;

import com.codecool.spaceship.model.ship.shipparts.EngineManager;

public record EngineDTO(int level, int speed, boolean fullyUpgraded) {

    public EngineDTO(EngineManager engineManager) {
        this(engineManager.getCurrentLevel(), engineManager.getSpeed(), engineManager.isFullyUpgraded());
    }
}
