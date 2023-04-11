package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.ship.NewShipDTO;
import com.codecool.spaceship.model.dto.ship.ShipDTO;
import com.codecool.spaceship.model.dto.ship.ShipDetailDTO;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.ShipType;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.ship.shipparts.ShipPart;
import com.codecool.spaceship.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/ship")
@CrossOrigin(origins = "http://localhost:3000")
public class ShipController {

    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/all/{stationId}")
    public List<ShipDTO> getAllShipsFromBase(@PathVariable Long stationId) {
        return shipService.getShipsByStation(stationId);
    }

    @GetMapping("/{id}")
    public ShipDTO getShipById(@PathVariable Long id) {
        return shipService.getShipByID(id);
    }

    @GetMapping("/{id}/detail")
    public ShipDetailDTO getShipDetailsById(@PathVariable Long id) {
        return shipService.getShipDetailsById(id);

    }

    @GetMapping("/{id}/upgrade")
    public Map<ResourceType, Integer> getShipPartUpgradeCost(@PathVariable Long id, @RequestParam ShipPart part) {
        return shipService.getShipPartUpgradeCost(id, part);
    }

    @PatchMapping("/{id}/upgrade")
    public ShipDetailDTO upgradeShipPart(@PathVariable Long id, @RequestParam ShipPart part) {
        return shipService.upgradeShipPart(id, part);
    }

    @GetMapping("/color")
    public Color[] getAvailableColors() {
        return shipService.getColors();
    }

    @PatchMapping("/{id}")
    public ShipDTO updateShipAttributes(@PathVariable Long id, @RequestBody NewShipDTO shipDTO) {
        return shipService.updateShipAttributes(id, shipDTO.name(), shipDTO.color());
    }

    @DeleteMapping("/{id}")
    public Boolean deleteShipById(@PathVariable Long id) {
        return shipService.deleteShipById(id);

    }

    @GetMapping("/cost/{shipType}")
    public Map<ResourceType, Integer> getMinerShipCost(@PathVariable String shipType) {
        return shipService.getShipCost(ShipType.valueOf(shipType.toUpperCase()));
    }

}
