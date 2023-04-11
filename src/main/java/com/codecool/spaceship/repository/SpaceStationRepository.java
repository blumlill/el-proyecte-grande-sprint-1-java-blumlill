package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.station.SpaceStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpaceStationRepository extends JpaRepository<SpaceStation, Long> {

    Optional<SpaceStation> getSpaceStationByUser(UserEntity user);
}
