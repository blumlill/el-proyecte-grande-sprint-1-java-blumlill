package com.codecool.spaceship.model.dto.ship;

import com.codecool.spaceship.model.dto.ship.part.EngineDTO;
import com.codecool.spaceship.model.dto.ship.part.ScannerDTO;
import com.codecool.spaceship.model.dto.ship.part.ShieldDTO;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import lombok.Getter;

@Getter
public class ScoutShipDTO extends ShipDetailDTO{

    private final ScannerDTO scanner;

    public ScoutShipDTO(long id, String name, Color color, ShipType type, Mission mission, EngineDTO engineDTO,
                        ShieldDTO shieldDTO, ScannerDTO scanner) {
        super(id, name, color, type, mission, engineDTO, shieldDTO);
        this.scanner = scanner;
    }
}
