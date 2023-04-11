package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.dto.ship.ScoutShipDTO;
import com.codecool.spaceship.model.dto.ship.ShipDetailDTO;
import com.codecool.spaceship.model.dto.ship.part.EngineDTO;
import com.codecool.spaceship.model.dto.ship.part.ScannerDTO;
import com.codecool.spaceship.model.dto.ship.part.ShieldDTO;
import com.codecool.spaceship.model.exception.NoSuchPartException;
import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ScannerManager;
import com.codecool.spaceship.model.ship.shipparts.ShieldManager;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.service.LevelService;

import java.util.Map;
import java.util.Set;

public class ScoutShipManager extends SpaceShipManager{

    private static final Set<ShipPart> PARTS = Set.of(ShipPart.ENGINE, ShipPart.SHIELD, ShipPart.SCANNER);
    private final ScoutShip scoutShip;
    private ScannerManager scanner;

    protected ScoutShipManager(LevelService levelService, ScoutShip scoutShip) {
        super(levelService, scoutShip);
        this.scoutShip = scoutShip;
    }

    public static ScoutShip createNewScoutShip(LevelService levelService, String name, Color color) {
        ScoutShip ship = new ScoutShip();
        ship.setName(name);
        ship.setColor(color);
        ship.setEngineLevel(1);
        ship.setShieldLevel(1);
        ship.setShieldEnergy(new ShieldManager(levelService).getMaxEnergy());
        ship.setScannerLevel(1);
        return ship;
    }

    public int getScannerEfficiency() {
        createScannerIfNotExists();
        return scanner.getEfficiency();
    }

    @Override
    public ShipDetailDTO getDetailedDTO() {
        createEngineIfNotExists();
        createShieldIfNotExists();
        createScannerIfNotExists();
        return new ScoutShipDTO(
                scoutShip.getId(),
                scoutShip.getName(),
                scoutShip.getColor(),
                ShipType.SCOUT,
                scoutShip.getCurrentMission(),
                new EngineDTO(engine),
                new ShieldDTO(shield),
                new ScannerDTO(scanner)
        );
    }


    @Override
    public Set<ShipPart> getPartTypes() {
        return PARTS;
    }

    @Override
    public boolean upgradePart(ShipPart part) throws NoSuchPartException {
        switch (part) {
            case ENGINE -> {
                createEngineIfNotExists();
                engine.upgrade();
                scoutShip.setEngineLevel(engine.getCurrentLevel());
            }
            case SHIELD -> {
                createShieldIfNotExists();
                shield.upgrade();
                shield.setEnergyToMax();
                scoutShip.setShieldLevel(shield.getCurrentLevel());
                scoutShip.setShieldEnergy(shield.getCurrentEnergy());
            }
            case SCANNER -> {
                createScannerIfNotExists();
                scanner.upgrade();
                scoutShip.setScannerLevel(scanner.getCurrentLevel());
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
        return true;
    }

    @Override
    public Map<ResourceType, Integer> getCost() {
        return ShipType.SCOUT.getCost();
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
            case SCANNER -> {
                createScannerIfNotExists();
                return scanner.getUpgradeCost();
            }
            default -> throw new NoSuchPartException("No such part on this ship");
        }
    }

    private void createScannerIfNotExists() {
        if (scanner == null) {
            scanner = new ScannerManager(levelService, scoutShip.getScannerLevel());
        }
    }
}
