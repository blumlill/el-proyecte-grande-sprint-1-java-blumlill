package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.station.StationStorageManager;

import java.util.Map;

public record SpaceStationStorageDTO(Map<ResourceType, Integer> resources, int level, int capacity, int freeSpace, boolean fullyUpgraded) {

    public SpaceStationStorageDTO(StationStorageManager storage) {
        this(storage.getStoredResources(), storage.getCurrentLevel(), storage.getCurrentCapacity(),
                storage.getCurrentAvailableStorageSpace(), storage.isFullyUpgraded());
    }

}
