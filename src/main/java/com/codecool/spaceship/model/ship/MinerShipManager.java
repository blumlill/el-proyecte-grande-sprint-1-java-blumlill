package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.dto.ship.MinerShipDTO;
import com.codecool.spaceship.model.dto.ship.ShipDetailDTO;
import com.codecool.spaceship.model.dto.ship.part.DrillDTO;
import com.codecool.spaceship.model.dto.ship.part.EngineDTO;
import com.codecool.spaceship.model.dto.ship.part.ShieldDTO;
import com.codecool.spaceship.model.dto.ship.part.ShipStorageDTO;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.shipparts.*;
import com.codecool.spaceship.service.LevelService;

import java.util.*;

public class MinerShipManager extends SpaceShipManager {

    private static final Set<ShipPart> PARTS = Set.of(ShipPart.ENGINE, ShipPart.SHIELD, ShipPart.DRILL, ShipPart.STORAGE);
    private final MinerShip minerShip;
    private ShipStorageManager storage;
    private DrillManager drill;

    public MinerShipManager(LevelService levelService, MinerShip minerShip) {
        super(levelService, minerShip);
        this.minerShip = minerShip;
    }

    public static MinerShip createNewMinerShip(LevelService levelService, String name, Color color) {
        MinerShip ship = new MinerShip();
        ship.setName(name);
        ship.setColor(color);
        ship.setEngineLevel(1);
        ship.setShieldLevel(1);
        ship.setShieldEnergy(new ShieldManager(levelService).getMaxEnergy());
        ship.setDrillLevel(1);
        ship.setStorageLevel(1);
        ship.setStoredResources(new HashMap<>());
        return ship;
    }

    @Override
    public ShipDetailDTO getDetailedDTO() {
        createEngineIfNotExists();
        createShieldIfNotExists();
        createDrillIfNotExists();
        createStorageIfNotExists();
        return new MinerShipDTO(
                minerShip.getId(),
                minerShip.getName(),
                minerShip.getColor(),
                ShipType.MINER,
                minerShip.getCurrentMission(),
                new EngineDTO(engine),
                new ShieldDTO(shield),
                new DrillDTO(drill),
                new ShipStorageDTO(storage));
    }

    public int getDrillEfficiency() {
        createDrillIfNotExists();
        return drill.getEfficiency();
    }

    public int getMaxStorageCapacity() {
        createStorageIfNotExists();
        return storage.getCurrentCapacity();
    }

    public int getEmptyStorageSpace() {
        createStorageIfNotExists();
        return storage.getCurrentAvailableStorageSpace();
    }

    public Map<ResourceType, Integer> getStorageContents() {
        createStorageIfNotExists();
        return storage.getStoredResources();
    }

    public boolean hasResourcesInStorage(Map<ResourceType, Integer> resources) {
        createStorageIfNotExists();
        return resources.entrySet().stream()
                .allMatch(entry ->  storage.hasResource(entry.getKey(), entry.getValue()));
    }

    public boolean addResourceToStorage(ResourceType resourceType, int amount) throws StorageException {
        createStorageIfNotExists();
        return storage.addResource(resourceType, amount);
    }

    public boolean removeResourceFromStorage(ResourceType resourceType, int amount) throws StorageException {
        createStorageIfNotExists();
        return storage.removeResource(resourceType, amount);
    }
    @Override
    public Set<ShipPart> getPartTypes() {
        return PARTS;
    }

    @Override
    public Map<ResourceType, Integer> getUpgradeCost(ShipPart part) throws UpgradeNotAvailableException, NoSuchPartException {
        switch (part) {
            case ENGINE -> {
                createEngineIfNotExists();
                return engine.getUpgradeCost();
            }
            case SHIELD -> {
                createShieldIfNotExists();
                return shield.getUpgradeCost();
            }
            case DRILL -> {
                createDrillIfNotExists();
                return drill.getUpgradeCost();
            }
            case STORAGE -> {
                createStorageIfNotExists();
                return storage.getUpgradeCost();
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
    }

    @Override
    public boolean upgradePart(ShipPart part) throws NoSuchPartException {
        switch (part) {
            case ENGINE -> {
                createEngineIfNotExists();
                engine.upgrade();
                minerShip.setEngineLevel(engine.getCurrentLevel());
            }
            case SHIELD -> {
                createShieldIfNotExists();
                shield.upgrade();
                shield.setEnergyToMax();
                minerShip.setShieldLevel(shield.getCurrentLevel());
                minerShip.setShieldEnergy(shield.getCurrentEnergy());
            }
            case DRILL -> {
                createDrillIfNotExists();
                drill.upgrade();
                minerShip.setDrillLevel(drill.getCurrentLevel());
            }
            case STORAGE -> {
                createStorageIfNotExists();
                storage.upgrade();
                minerShip.setStorageLevel(storage.getCurrentLevel());
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
        return true;
    }

    @Override
    public Map<ResourceType, Integer> getCost() {
        return new HashMap<>(ShipType.MINER.getCost());
    }

    private void createStorageIfNotExists() {
        if (storage == null) {
            storage = new ShipStorageManager(levelService, minerShip.getStorageLevel(), minerShip.getStoredResources());
        }
    }

    private void createDrillIfNotExists() {
        if (drill == null) {
            drill = new DrillManager(levelService, minerShip.getDrillLevel());
        }
    }
}
