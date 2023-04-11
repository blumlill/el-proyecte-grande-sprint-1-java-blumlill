package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.resource.ResourceType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findAllByUserAndResourceTypeInAndResourceReserveGreaterThan(UserEntity user,
                                                                               Collection<ResourceType> resources,
                                                                               int reserveGreaterThan, Sort sort);
}
