package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.station.SpaceStation;

public record SpaceStationDataDTO(long id, String name) {

    public SpaceStationDataDTO(SpaceStation station) {
        this(station.getId(), station.getName());
    }
}
