package com.codecool.spaceship.model.dto.ship;


import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.ScoutShip;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.model.ship.shipparts.Color;
import lombok.*;

@Getter
@AllArgsConstructor
public class ShipDTO {
    private final long id;
    private final String name;
    private final Color color;
    private final ShipType type;
    private final long missionId;

    public ShipDTO(long id, String name, Color color, ShipType type, Mission mission) {
        this(id, name, color, type, getCurrentMissionId(mission));
    }
    public ShipDTO(SpaceShip ship) {
        this(ship.getId(), ship.getName(), ship.getColor(), getShipType(ship), getCurrentMissionId(ship.getCurrentMission()));
    }

    private static ShipType getShipType(SpaceShip ship) {
        if (ship instanceof MinerShip) {
            return ShipType.MINER;
        } else if (ship instanceof ScoutShip){
            return ShipType.SCOUT;
        } else {
            throw new RuntimeException("Unrecognized ship type");
        }
    }

    private static Long getCurrentMissionId(Mission mission) {
        if (mission == null) {
            return 0L;
        } else {
            return mission.getId();
        }
    }
}
