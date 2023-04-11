package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.ResourceType;

import java.util.Map;

public enum ShipType {

    MINER(Map.of(
            ResourceType.METAL, 50,
            ResourceType.CRYSTAL, 20,
            ResourceType.SILICONE, 20
    )),

    SCOUT(Map.of(
            ResourceType.METAL, 10,
            ResourceType.CRYSTAL, 10,
            ResourceType.SILICONE, 10
    ));

    private final Map<ResourceType, Integer> cost;

    ShipType(Map<ResourceType, Integer> cost) {
        this.cost = cost;
    }

    public Map<ResourceType, Integer> getCost() {
        return cost;
    }
}
