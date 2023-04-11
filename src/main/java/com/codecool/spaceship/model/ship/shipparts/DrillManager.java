package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.service.LevelService;

public class DrillManager extends Upgradeable {

    private static final UpgradeableType TYPE = UpgradeableType.DRILL;

    public DrillManager(LevelService levelService, int currentLevel) {
        super(levelService, TYPE, currentLevel);
    }
    public DrillManager(LevelService levelService) {
        this(levelService, 1);
    }

    public int getEfficiency() {
        return currentLevel.getEffect();
    }
}
