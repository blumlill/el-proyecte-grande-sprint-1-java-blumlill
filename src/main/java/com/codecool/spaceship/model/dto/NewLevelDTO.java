package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.resource.ResourceType;
import lombok.NonNull;


import java.util.Map;

public record NewLevelDTO(@NonNull UpgradeableType type, int effect, Map<ResourceType, Integer> cost) {
}
