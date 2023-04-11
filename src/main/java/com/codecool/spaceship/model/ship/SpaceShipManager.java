package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.dto.ship.ShipDetailDTO;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.shipparts.EngineManager;
import com.codecool.spaceship.model.ship.shipparts.ShieldManager;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.service.LevelService;

import java.util.Map;
import java.util.Set;


public abstract class SpaceShipManager {

    protected final LevelService levelService;
    protected final SpaceShip spaceShip;
    protected ShieldManager shield;
    protected EngineManager engine;

    protected SpaceShipManager(LevelService levelService, SpaceShip spaceShip) {
        this.levelService = levelService;
        this.spaceShip = spaceShip;
    }

    public SpaceShip getShip() {
       return spaceShip;
    }

    public boolean isAvailable() {
        return spaceShip.getCurrentMission() == null;
    }

    public Mission getCurrentMission() {
        return spaceShip.getCurrentMission();
    }

    public void setCurrentMission(Mission mission) {
        spaceShip.setCurrentMission(mission);
    }

    public void endMission() {
        spaceShip.setCurrentMission(null);
    }

    public int getShieldEnergy() {
        return spaceShip.getShieldEnergy();
    }

    public int getShieldMaxEnergy() {
        createShieldIfNotExists();
        return shield.getMaxEnergy();
    }

    public void repairShield(int amount) {
        createShieldIfNotExists();
        shield.repair(amount);
    }

    public void damageShield(int amount) {
        createShieldIfNotExists();
        shield.damage(amount);
    }

    public int getSpeed() {
        createEngineIfNotExists();
        return engine.getSpeed();
    }

    protected void createShieldIfNotExists() {
        if (shield == null) {
            shield = new ShieldManager(levelService, spaceShip.getShieldLevel(), spaceShip.getShieldEnergy());
        }
    }

    protected void createEngineIfNotExists() {
        if (engine == null) {
            engine = new EngineManager(levelService, spaceShip.getEngineLevel());
        }
    }

    public abstract ShipDetailDTO getDetailedDTO();

    public abstract Set<ShipPart> getPartTypes();

    public abstract boolean upgradePart(ShipPart part) throws NoSuchPartException;

    public abstract Map<ResourceType, Integer> getCost();

    public abstract Map<ResourceType, Integer> getUpgradeCost(ShipPart part) throws UpgradeNotAvailableException, NoSuchPartException;
}
