package com.codecool.spaceship.model.dto.ship.part;

import com.codecool.spaceship.model.ship.shipparts.DrillManager;

public record DrillDTO(int level, int efficiency, boolean fullyUpgraded){

    public DrillDTO(DrillManager drillManager) {
        this(drillManager.getCurrentLevel(), drillManager.getEfficiency(), drillManager.isFullyUpgraded());
    }
}
