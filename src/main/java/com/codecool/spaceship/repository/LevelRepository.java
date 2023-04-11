package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.UpgradeableType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LevelRepository extends JpaRepository<Level, Long> {

    Optional<Level> getLevelByTypeAndLevel(UpgradeableType type, int level);

    List<Level> getLevelsByType(UpgradeableType type);

    Optional<Level> getLevelByTypeAndMax(UpgradeableType type, boolean isMax);
}
