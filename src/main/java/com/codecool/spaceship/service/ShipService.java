package com.codecool.spaceship.service;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.dto.ship.ShipDTO;
import com.codecool.spaceship.model.dto.ship.ShipDetailDTO;
import com.codecool.spaceship.model.exception.*;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.mission.MissionManager;
import com.codecool.spaceship.model.mission.MissionFactory;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.*;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.MissionRepository;
import com.codecool.spaceship.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShipService {
    private final SpaceShipRepository spaceShipRepository;
    private final MissionRepository missionRepository;
    private final ShipManagerFactory shipManagerFactory;
    private final MissionFactory missionFactory;
    private final LevelService levelService;

    @Autowired
    public ShipService(SpaceShipRepository spaceShipRepository, MissionRepository missionRepository, ShipManagerFactory shipManagerFactory, MissionFactory missionFactory, LevelService levelService) {
        this.spaceShipRepository = spaceShipRepository;
        this.missionRepository = missionRepository;
        this.shipManagerFactory = shipManagerFactory;
        this.missionFactory = missionFactory;
        this.levelService = levelService;
    }

    public List<ShipDTO> getShipsByStation(long stationId) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                && user.getSpaceStation().getId() != stationId) {
            throw new SecurityException("You don't have authority to access these ships");
        }
        return spaceShipRepository.getSpaceShipsByStationId(stationId).stream()
                .map(ShipDTO::new)
                .collect(Collectors.toList());
    }

    public ShipDTO getShipByID(long id) throws DataNotFoundException {
        return new ShipDTO(getShipByIdAndCheckAccess(id));
    }

    public ShipDetailDTO getShipDetailsById(long id) throws DataNotFoundException, IllegalArgumentException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(ship);
        updateMissionIfExists(spaceShipManager);
        return spaceShipManager.getDetailedDTO();
    }

    public ShipDetailDTO upgradeShipPart(Long id, ShipPart part) throws DataNotFoundException, UpgradeNotAvailableException, NoSuchPartException, StorageException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(ship);
        SpaceStationManager stationManager = new SpaceStationManager(ship.getStation(), levelService);
        if (stationManager.hasShipAvailable(ship)) {
            stationManager.removeResources(spaceShipManager.getUpgradeCost(part));
            spaceShipManager.upgradePart(part);
            spaceShipRepository.save(ship);
        }
        return spaceShipManager.getDetailedDTO();
    }

    public ShipDTO updateShipAttributes(Long id, String name, Color color) throws DataNotFoundException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);

        if (name != null && !name.equals("")) {
            ship.setName(name);
        }
        if (color != null) {
            ship.setColor(color);
        }
        ship = spaceShipRepository.save(ship);
        return new ShipDTO(ship);
    }

    public Map<ResourceType, Integer> getShipCost(ShipType shipType) {
        if (shipType != null) {
            return shipType.getCost();
        } else {
            return null;
        }
    }

    public Color[] getColors() {
        return Color.values();
    }

    public Map<ResourceType, Integer> getShipPartUpgradeCost(Long id, ShipPart shipPart) throws DataNotFoundException, UpgradeNotAvailableException, NoSuchPartException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);
        SpaceShipManager spaceShipManager = shipManagerFactory.getSpaceShipManager(ship);
        return spaceShipManager.getUpgradeCost(shipPart);
    }

    public boolean deleteShipById(Long id) throws StorageException, DataNotFoundException {
        SpaceShip ship = getShipByIdAndCheckAccess(id);

        if (ship.getCurrentMission() != null) {
            throw new StorageException("Ship can't be deleted while on mission");
        }
        spaceShipRepository.delete(ship);
        return true;
    }

    private void updateMissionIfExists(SpaceShipManager spaceShipManager) {
        Mission currentMission = spaceShipManager.getCurrentMission();
        if (currentMission != null) {
            MissionManager missionManager = missionFactory.getMissionManager(currentMission);
            missionManager.setShipManager(spaceShipManager);
            if (missionManager.updateStatus()) {
                missionRepository.save(currentMission);
            }
        }
    }

    private SpaceShip getShipByIdAndCheckAccess(Long id) throws DataNotFoundException {
        SpaceShip ship = spaceShipRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d".formatted(id)));
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || Objects.equals(user.getId(), ship.getUser().getId())) {
            return ship;
        } else {
            throw new SecurityException("You don't have authority to access this ship");
        }
    }

}
