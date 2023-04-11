package com.codecool.spaceship.service;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.dto.LevelDTO;
import com.codecool.spaceship.model.dto.NewLevelDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class LevelService {

    private final LevelRepository levelRepository;

    @Autowired
    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    public List<UpgradeableType> getLevelTypes() {
        return Arrays.stream(UpgradeableType.values()).toList();
    }

    public Level getLevelByTypeAndLevel(UpgradeableType type, int level) {
        return levelRepository.getLevelByTypeAndLevel(type, level)
                .orElseThrow(() -> new IllegalArgumentException(type + " has no level " + level + "."));
    }

    public List<LevelDTO> getLevelsByType(UpgradeableType type) {
        return levelRepository.getLevelsByType(type).stream()
                .map(LevelDTO::new)
                .toList();
    }

    public LevelDTO updateLevelById(Long id, NewLevelDTO newLevelDTO) {
        Level level = levelRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Level not found"));
        level.setEffect(newLevelDTO.effect());
        level.setCost(newLevelDTO.cost());
        levelRepository.save(level);
        return new LevelDTO(level);
    }

    public LevelDTO addNewLevel(NewLevelDTO newLevelDTO) {
        Level prevMaxLevel = levelRepository.getLevelByTypeAndMax(newLevelDTO.type(), true)
                .orElse(null);
        Level newMaxLevel = Level.builder()
                .level(prevMaxLevel == null ? 1 : prevMaxLevel.getLevel() + 1)
                .type(newLevelDTO.type())
                .effect(newLevelDTO.effect())
                .cost(newLevelDTO.cost())
                .max(true)
                .build();
        if (prevMaxLevel != null) {
            prevMaxLevel.setMax(false);
            levelRepository.save(prevMaxLevel);
        }
        newMaxLevel = levelRepository.save(newMaxLevel);
        return new LevelDTO(newMaxLevel);
    }

    public boolean deleteLastLevelOfType(UpgradeableType type) {
        Level maxLevel = levelRepository.getLevelByTypeAndMax(type, true)
                .orElseThrow(() -> new RuntimeException("No max level has been set for %s type".formatted(type)));
        if (maxLevel.getLevel() == 1) {
            throw new IllegalArgumentException("The first level can't be deleted.");
        }
        Level newMaxLevel = levelRepository.getLevelByTypeAndLevel(type, maxLevel.getLevel() - 1)
                .orElseThrow(() -> new RuntimeException("Level sequence is incorrect for %s type".formatted(type)));
        newMaxLevel.setMax(true);
        levelRepository.delete(maxLevel);
        levelRepository.save(newMaxLevel);
        return true;
    }

}
