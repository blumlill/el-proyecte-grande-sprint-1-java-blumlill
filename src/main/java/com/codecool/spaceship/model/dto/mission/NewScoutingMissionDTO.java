package com.codecool.spaceship.model.dto.mission;

import com.codecool.spaceship.model.resource.ResourceType;
import lombok.NonNull;

public record NewScoutingMissionDTO(long shipId, int distance, long activityTime, @NonNull ResourceType targetResource, boolean prioritizingDistance) {
}
