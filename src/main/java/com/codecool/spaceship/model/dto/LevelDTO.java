package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.resource.ResourceType;

import java.util.Map;

public record LevelDTO(long id, UpgradeableType type, int level, int effect, Map<ResourceType, Integer> cost, boolean max) {

    public LevelDTO(Level level) {
        this(level.getId(), level.getType(), level.getLevel(), level.getEffect(), level.getCost(), level.isMax());
    }
}
