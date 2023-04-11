package com.codecool.spaceship.model.dto.ship.part;

import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.shipparts.ShipStorageManager;

import java.util.Map;

public record ShipStorageDTO(int level, int maxCapacity, Map<ResourceType, Integer> resources, boolean fullyUpgraded) {

    public ShipStorageDTO(ShipStorageManager shipStorageManager) {
        this(shipStorageManager.getCurrentLevel(), shipStorageManager.getCurrentCapacity(), shipStorageManager.getStoredResources(), shipStorageManager.isFullyUpgraded());
    }
}
