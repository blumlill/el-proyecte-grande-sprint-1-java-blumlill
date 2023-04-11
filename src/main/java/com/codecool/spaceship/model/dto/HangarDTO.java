package com.codecool.spaceship.model.dto;

import com.codecool.spaceship.model.dto.ship.ShipDTO;
import com.codecool.spaceship.model.station.HangarManager;

import java.util.Set;
import java.util.stream.Collectors;

public record HangarDTO(Set<ShipDTO> ships, int level, int capacity, int freeDocks, boolean fullyUpgraded) {

    public HangarDTO(HangarManager hangarManager) {
        this(getAllShipDTOs(hangarManager), hangarManager.getCurrentLevel(), hangarManager.getCurrentCapacity(),
                hangarManager.getCurrentAvailableDocks(), hangarManager.isFullyUpgraded());
    }

    public static Set<ShipDTO> getAllShipDTOs(HangarManager hangar) {
        return hangar.getAllShips().stream().map(ShipDTO::new).collect(Collectors.toSet());
    }

}
