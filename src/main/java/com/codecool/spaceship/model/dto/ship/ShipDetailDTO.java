package com.codecool.spaceship.model.dto.ship;

import com.codecool.spaceship.model.dto.ship.part.EngineDTO;
import com.codecool.spaceship.model.dto.ship.part.ShieldDTO;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import lombok.Getter;

@Getter
public abstract class ShipDetailDTO extends ShipDTO {
    private final EngineDTO engine;
    private final ShieldDTO shield;

    public ShipDetailDTO(long id, String name, Color color, ShipType type, Mission mission, EngineDTO engine, ShieldDTO shield) {
        super(id, name, color, type, mission);
        this.engine = engine;
        this.shield = shield;
    }

}
