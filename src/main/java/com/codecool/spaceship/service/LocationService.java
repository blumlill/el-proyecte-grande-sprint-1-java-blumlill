package com.codecool.spaceship.service;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.dto.LocationDTO;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getAllLocationsForCurrentUser(List<ResourceType> resourceTypes, int reserveGreaterThan, String orderBy, boolean asc) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Sort sort = Sort.by(asc ? Sort.Order.asc(orderBy) : Sort.Order.desc(orderBy));
        return locationRepository
                .findAllByUserAndResourceTypeInAndResourceReserveGreaterThan(user, resourceTypes,reserveGreaterThan, sort)
                .stream()
                .map(LocationDTO::new)
                .toList();
    }
}
