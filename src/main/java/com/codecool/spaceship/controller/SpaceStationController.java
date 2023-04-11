package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.HangarDTO;
import com.codecool.spaceship.model.dto.SpaceStationDataDTO;
import com.codecool.spaceship.model.dto.ship.NewShipDTO;
import com.codecool.spaceship.model.dto.SpaceStationDTO;
import com.codecool.spaceship.model.dto.SpaceStationStorageDTO;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/base")
@CrossOrigin(origins = "http://localhost:3000")
public class SpaceStationController {

    private final StationService stationService;

    @Autowired
    public SpaceStationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping({"/{baseId}"})
    public SpaceStationDTO getBaseById(@PathVariable long baseId) {
        return stationService.getBaseById(baseId);
    }

    @GetMapping({"/"})
    public SpaceStationDataDTO getBaseDataForCurrentUser() {
        return stationService.getBaseDataForCurrentUser();
    }

    @PostMapping("/{baseId}/add/resources")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public Boolean addResources(@PathVariable long baseId, @RequestBody Map<ResourceType, Integer> resources) {
        return stationService.addResources(baseId, resources);
    }

    @PatchMapping("/{baseId}/add/resource-from-ship")
    public Boolean moveResourceFromShipToStation(@PathVariable long baseId, @RequestParam(name = "ship") long shipId, @RequestBody Map<ResourceType, Integer> resources) {
        return stationService.moveResourceFromShipToStation(baseId, shipId, resources);
    }

    @PostMapping("/{baseId}/add/ship")
    public Long addShip(@PathVariable long baseId, @RequestBody NewShipDTO newShipDTO) {
        return stationService.addShip(baseId, newShipDTO);
    }

    @GetMapping("/{baseId}/storage")
    public SpaceStationStorageDTO getStationStorage(@PathVariable long baseId) {
        return stationService.getStationStorage(baseId);
    }

    @GetMapping("/{baseId}/storage/resources")
    public Map<ResourceType, Integer> getStoredResources(@PathVariable long baseId) {
        return stationService.getStoredResources(baseId);
    }

    @GetMapping("/{baseId}/storage/upgrade")
    public Map<ResourceType, Integer> getStorageUpgradeCost(@PathVariable long baseId) {
        return stationService.getStorageUpgradeCost(baseId);
    }

    @PostMapping("/{baseId}/storage/upgrade")
    public Boolean upgradeStorage(@PathVariable long baseId) {
        return stationService.upgradeStorage(baseId);
    }

    @GetMapping("/{baseId}/hangar")
    public HangarDTO getStationHangar(@PathVariable long baseId) {
        return stationService.getStationHangar(baseId);
    }

    @GetMapping("/{baseId}/hangar/upgrade")
    public Map<ResourceType, Integer> getHangarUpgradeCost(@PathVariable long baseId) {
        return stationService.getHangarUpgradeCost(baseId);
    }

    @PostMapping("/{baseId}/hangar/upgrade")
    public Boolean upgradeHangar(@PathVariable long baseId) {
        return stationService.upgradeHangar(baseId);
    }

}
