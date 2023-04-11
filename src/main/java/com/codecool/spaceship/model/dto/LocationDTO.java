package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.resource.ResourceType;

import java.time.LocalDateTime;

public record LocationDTO(long id,
                          String name,
                          ResourceType resourceType,
                          LocationReserve resourceReserve,
                          int distanceFromStation,
                          LocalDateTime discovered,
                          long missionId) {

    public LocationDTO(Location location) {
        this(location.getId(), location.getName(), location.getResourceType(), assignReserve(location.getResourceReserve()),
                location.getDistanceFromStation(), location.getDiscovered(), getMissionId(location.getCurrentMission()));
    }

    private static Long getMissionId(Mission mission) {
        if (mission == null) {
            return 0L;
        } else return mission.getId();
    }

    private static LocationReserve assignReserve(int reserve) {
        if (reserve <= 0) {
            return LocationReserve.DEPLETED;
        } else if (reserve <= 100){
            return LocationReserve.POOR;
        } else if (reserve <= 500) {
            return LocationReserve.MODERATE;
        } else if (reserve <= 800) {
            return LocationReserve.GOOD;
        } else {
            return LocationReserve.RICH;
        }
    }
}
