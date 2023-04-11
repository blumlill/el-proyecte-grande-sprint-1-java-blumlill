package com.codecool.spaceship.model.ship.shipparts;

import com.codecool.spaceship.model.AbstractStorageManager;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.service.LevelService;

import java.util.HashMap;
import java.util.Map;

public class ShipStorageManager extends AbstractStorageManager {

    private static final UpgradeableType TYPE = UpgradeableType.SHIP_STORAGE;

    public ShipStorageManager(LevelService levelService, int currentLevel, Map<ResourceType, Integer> storedResources) {
        super(levelService, TYPE, currentLevel, storedResources);
    }
    public ShipStorageManager(LevelService levelService) {
        this(levelService, 1, new HashMap<>());
    }

}
