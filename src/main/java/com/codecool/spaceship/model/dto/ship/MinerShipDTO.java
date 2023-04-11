package com.codecool.spaceship.model.dto.ship;


import com.codecool.spaceship.model.dto.ship.part.DrillDTO;
import com.codecool.spaceship.model.dto.ship.part.EngineDTO;
import com.codecool.spaceship.model.dto.ship.part.ShieldDTO;
import com.codecool.spaceship.model.dto.ship.part.ShipStorageDTO;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import lombok.Getter;

@Getter
public class MinerShipDTO extends ShipDetailDTO {
    private final DrillDTO drill;
    private final ShipStorageDTO storage;

    public MinerShipDTO(long id, String name, Color color, ShipType type, Mission mission, EngineDTO engineDTO,
                        ShieldDTO shieldDTO, DrillDTO drillDTO, ShipStorageDTO storageDTO) {
        super(id, name, color, type, mission, engineDTO, shieldDTO);
        this.drill = drillDTO;
        this.storage = storageDTO;
    }
}
