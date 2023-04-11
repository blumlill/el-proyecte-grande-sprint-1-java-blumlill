package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.AbstractStorageManager;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.service.LevelService;

import java.util.HashMap;
import java.util.Map;

public class StationStorageManager extends AbstractStorageManager {

    private static final UpgradeableType TYPE = UpgradeableType.STATION_STORAGE;


    public StationStorageManager(LevelService levelService, int currentLevel, Map<ResourceType, Integer> storedResources) {
        super(levelService, TYPE, currentLevel, storedResources);
    }
    public StationStorageManager(LevelService levelService) {
        this(levelService, 1, new HashMap<>());
    }

}
