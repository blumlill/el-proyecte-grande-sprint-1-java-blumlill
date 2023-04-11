package com.codecool.spaceship.model.dto.ship.part;

import com.codecool.spaceship.model.ship.shipparts.ShieldManager;

public record ShieldDTO(int level, int energy, int maxEnergy, boolean fullyUpgraded){

    public ShieldDTO(ShieldManager shieldManager) {
        this(shieldManager.getCurrentLevel(), shieldManager.getCurrentEnergy(), shieldManager.getMaxEnergy(), shieldManager.isFullyUpgraded());
    }
}
