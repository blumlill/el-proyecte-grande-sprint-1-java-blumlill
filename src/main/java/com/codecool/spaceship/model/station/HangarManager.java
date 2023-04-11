package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Upgradeable;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.service.LevelService;

import java.util.HashSet;
import java.util.Set;

public class HangarManager extends Upgradeable {

    private static final UpgradeableType TYPE = UpgradeableType.HANGAR;
    private final Set<SpaceShip> shipSet;

    public HangarManager(LevelService levelService, int currentLevel, Set<SpaceShip> shipSet) {
        super(levelService, TYPE, currentLevel);
        if (shipSet.size() > super.currentLevel.getEffect()) {
            throw new StorageException("Ships in hangar can't exceed %d at this level.".formatted(super.currentLevel.getEffect()));
        }
        this.shipSet = shipSet;
    }

    public HangarManager(LevelService levelService) {
        this(levelService, 1, new HashSet<>());
    }

    public int getCurrentCapacity() {
        return currentLevel.getEffect();
    }

    public int getCurrentAvailableDocks() {
        return getCurrentCapacity() - shipSet.size();
    }

    public boolean addShip(SpaceShip ship) throws StorageException {
        if (getCurrentAvailableDocks() > 0) {
            return shipSet.add(ship);
        } throw new StorageException("No more docks available");
    }

    public boolean removeShip(SpaceShip ship) {
        return shipSet.remove(ship);
    }

    public Set<SpaceShip> getAllShips() {
        return new HashSet<>(shipSet);
    }

    public boolean hasShipAvailable(SpaceShip ship) throws StorageException {
        if (!shipSet.contains(ship)) {
            throw new StorageException("No such ship in storage");
        } else if (ship.getCurrentMission() != null) {
            throw new StorageException("Ship is on a mission");
        } else {
            return true;
        }
    }
}
