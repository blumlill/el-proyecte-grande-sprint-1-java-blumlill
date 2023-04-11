package com.codecool.spaceship.model.dto.ship.part;

import com.codecool.spaceship.model.ship.shipparts.ScannerManager;

public record ScannerDTO(int level, int efficiency, boolean fullyUpgraded) {

    public ScannerDTO(ScannerManager scannerManager) {
        this(scannerManager.getCurrentLevel(), scannerManager.getEfficiency(), scannerManager.isFullyUpgraded());
    }
}
