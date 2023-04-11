package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShipManagerFactory {
    private final LevelService levelService;
    @Autowired
    public ShipManagerFactory(LevelService levelService) {
        this.levelService = levelService;
    }

    public SpaceShipManager getSpaceShipManager(SpaceShip ship) {
        if (ship instanceof MinerShip) {
            return new MinerShipManager(levelService, (MinerShip) ship);
        } else if (ship instanceof ScoutShip) {
            return new ScoutShipManager(levelService, (ScoutShip) ship);
        } else {
            throw new IllegalArgumentException("This ship type is not supported.");
        }
    }
}
