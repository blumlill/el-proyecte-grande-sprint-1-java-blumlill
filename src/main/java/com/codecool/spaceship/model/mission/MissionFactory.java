package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.location.LocationDataGenerator;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.*;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MissionFactory {

    private final ShipManagerFactory shipManagerFactory;

    public MissionFactory(ShipManagerFactory shipManagerFactory) {
        this.shipManagerFactory = shipManagerFactory;
    }

    public MiningMission startNewMiningMission(MinerShip spaceShip, Location location, long activityDurationInSecs) {
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(spaceShip);
        if (spaceShipManager instanceof MinerShipManager) {
            return MiningMissionManager.startMiningMission((MinerShipManager) spaceShipManager, location, activityDurationInSecs);
        } else {
            throw new RuntimeException("Invalid ship manager.");
        }
    }

    public ScoutingMission startNewScoutingMission(ScoutShip spaceShip, int distance, long activityDurationInSecs,
                                                    ResourceType targetResource, boolean prioritizeDistance) {
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(spaceShip);
        if (spaceShipManager instanceof ScoutShipManager) {
            return ScoutingMissionManager.startScoutingMission((ScoutShipManager) spaceShipManager, distance, activityDurationInSecs, targetResource, prioritizeDistance);
        } else {
            throw new RuntimeException("Invalid ship manager.");
        }
    }
    public MissionManager getMissionManager(Mission mission) {
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(mission.getShip());
        if (mission instanceof MiningMission) {
            return new MiningMissionManager((MiningMission) mission, (MinerShipManager) spaceShipManager);
        } else if (mission instanceof  ScoutingMission) {
            return new ScoutingMissionManager((ScoutingMission) mission, (ScoutShipManager) spaceShipManager, new LocationDataGenerator(new Random()));
        } else {
            throw new RuntimeException("Mission type is not recognized");
        }
    }

}
