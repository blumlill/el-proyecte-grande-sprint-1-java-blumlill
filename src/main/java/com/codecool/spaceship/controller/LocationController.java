package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.dto.LocationDTO;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public List<LocationDTO> getAllLocationsForCurrentUser(@RequestParam(required = false) boolean includeDepleted,
                                                           @RequestParam(required = false) List<ResourceType> resources,
                                                           @RequestParam(required = false) String sort) {
        int reserveGreaterThan = includeDepleted ? -1 : 0;

        resources = (resources == null) ? List.of(ResourceType.values()) : resources;

        String orderBy;
        boolean asc;
        switch (sort) {
            case "discoveredASC" -> {
                orderBy = "discovered";
                asc = true;
            }
            case "nameASC" -> {
                orderBy = "name";
                asc = true;
            }
            case "nameDESC" -> {
                orderBy = "name";
                asc = false;
            }
            case "reserveASC" -> {
                orderBy = "resourceReserve";
                asc = true;
            }
            case "reserveDESC" -> {
                orderBy = "resourceReserve";
                asc = false;
            }
            case "distanceASC" -> {
                orderBy = "distanceFromStation";
                asc = true;
            }
            case "distanceDESC" -> {
                orderBy = "distanceFromStation";
                asc = false;
            }
            default -> {
                orderBy = "discovered";
                asc = false;
            }
        }
        return locationService.getAllLocationsForCurrentUser(resources, reserveGreaterThan, orderBy, asc);
    }
}
