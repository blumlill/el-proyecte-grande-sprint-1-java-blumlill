package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.service.LevelService;

public class ScannerManager extends Upgradeable {

    private static final UpgradeableType TYPE = UpgradeableType.SCANNER;

    public ScannerManager(LevelService levelService, int level) {
        super(levelService, TYPE, level);
    }

    public int getEfficiency() {
        return currentLevel.getEffect();
    }
}
