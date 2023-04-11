package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.ship.SpaceShip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceShipRepository extends JpaRepository<SpaceShip, Long> {

    List<SpaceShip> getSpaceShipsByStationId(long stationId);
}
