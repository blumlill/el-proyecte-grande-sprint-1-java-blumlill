package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.service.LevelService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpaceStationManager {

    private final SpaceStation station;
    private final LevelService levelService;
    private HangarManager hangar;
    private StationStorageManager storage;

    public SpaceStationManager(SpaceStation station, LevelService levelService) {
        this.station = station;
        this.levelService = levelService;
    }

    public static SpaceStation createNewSpaceStation(String name) {
        SpaceStation station = new SpaceStation();
        station.setName(name);
        station.setHangarLevel(1);
        station.setHangar(new HashSet<>());
        station.setStorageLevel(1);
        station.setStoredResources(new HashMap<>());
        return station;
    }

    private boolean hasEnoughResource(Map<ResourceType, Integer> cost) {
        createStorageIfNotExists();
        return cost.entrySet().stream()
                .allMatch(entry -> storage.hasResource(entry.getKey(), entry.getValue()));
    }

    public boolean removeResources(Map<ResourceType, Integer> cost) throws StorageException {
        createStorageIfNotExists();
        if (hasEnoughResource(cost)) {
            for (ResourceType resource : cost.keySet()) {
                storage.removeResource(resource, cost.get(resource));
            }
            return true;
        }
        throw new StorageException("Not enough resource");
    }


    public boolean addNewShip(SpaceShip ship, ShipType shipType) throws StorageException {
        Map<ResourceType, Integer> cost = shipType.getCost();
        createHangarIfNotExists();
        if (!hasEnoughResource(cost)) {
            throw new StorageException("Not enough resource");
        } else if (hangar.getCurrentAvailableDocks() == 0) {
            throw new StorageException("No more docks available");
        } else {
            return hangar.addShip(ship) && removeResources(cost); //throws storage exception if not enough resource or docks
        }
    }

    public boolean removeShip(SpaceShip ship) {
        createHangarIfNotExists();
        return hangar.removeShip(ship);
    }

    public Set<SpaceShip> getAllShips() {
        return new HashSet<>(station.getHangar());
    }

    public boolean hasShipAvailable(SpaceShip ship) throws StorageException {
        createHangarIfNotExists();
        return hangar.hasShipAvailable(ship);
    }

    //
    public boolean addResource(ResourceType resourceType, int quantity) throws StorageException {
        createStorageIfNotExists();
        return storage.addResource(resourceType, quantity);
    }
    public boolean addResourcesFromShip(MinerShipManager shipManager, Map<ResourceType, Integer> resources) throws StorageException {
        if (hasShipAvailable(shipManager.getShip()) && shipManager.hasResourcesInStorage(resources)) {
            int sum = resources.values().stream()
                    .mapToInt(i -> i)
                    .sum();
            createStorageIfNotExists();
            if (sum <= storage.getCurrentAvailableStorageSpace()) {
                for (ResourceType resource : resources.keySet()) {
                    addResource(resource, resources.get(resource));
                    shipManager.removeResourceFromStorage(resource, resources.get(resource));
                }
                return true;
            }
        }
        return false;
    }

    public Map<ResourceType, Integer> getStorageUpgradeCost() throws UpgradeNotAvailableException {
        createStorageIfNotExists();
        return storage.getUpgradeCost();
    }

    public Map<ResourceType, Integer> getHangarUpgradeCost() throws UpgradeNotAvailableException {
        createHangarIfNotExists();
        return hangar.getUpgradeCost();
    }

    public boolean upgradeStorage() throws UpgradeNotAvailableException, StorageException {
        createStorageIfNotExists();
        Map<ResourceType, Integer> cost = storage.getUpgradeCost();
        removeResources(cost);
        storage.upgrade();
        station.setStorageLevel(storage.getCurrentLevel());
        return true;
    }

    public boolean upgradeHangar() throws UpgradeNotAvailableException, StorageException {
        createHangarIfNotExists();
        Map<ResourceType, Integer> cost = hangar.getUpgradeCost();
        removeResources(cost);
        hangar.upgrade();
        station.setHangarLevel(hangar.getCurrentLevel());
        return true;
    }

    public SpaceStationDTO getStationDTO() {
        return new SpaceStationDTO(station.getId(), station.getName(), getHangarDTO(), getStorageDTO());
    }

    public SpaceStationStorageDTO getStorageDTO() {
        createStorageIfNotExists();
        return new SpaceStationStorageDTO(storage);
    }

    public HangarDTO getHangarDTO() {
        createHangarIfNotExists();
        return new HangarDTO(hangar);
    }

    public Map<ResourceType, Integer> getStoredResources() {
        createStorageIfNotExists();
        return storage.getStoredResources();
    }

    private void createHangarIfNotExists() {
        if (hangar == null) {
            hangar = new HangarManager(levelService, station.getHangarLevel(), station.getHangar());
        }
    }

    private void createStorageIfNotExists() {
        if (storage == null) {
            storage = new StationStorageManager(levelService, station.getStorageLevel(), station.getStoredResources());
        }
    }

}
