package com.codecool.spaceship.controller;

import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.dto.LevelDTO;
import com.codecool.spaceship.model.dto.NewLevelDTO;
import com.codecool.spaceship.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@RequestMapping("/api/v1/level")
public class LevelController {

    private final LevelService levelService;

    @Autowired
    public LevelController(LevelService levelService) {
        this.levelService = levelService;
    }

    @GetMapping("/types")
    public List<UpgradeableType> getLevelTypes() {
        return levelService.getLevelTypes();
    }

    @GetMapping
    public List<LevelDTO> getLevelsByType(@RequestParam UpgradeableType type) {
        return levelService.getLevelsByType(type);
    }


    @PatchMapping("/{id}")
    public LevelDTO updateLevelById(@PathVariable Long id, @RequestBody NewLevelDTO newLevelDTO) {
        return levelService.updateLevelById(id, newLevelDTO);
    }
    @PostMapping
    public LevelDTO addLewLevel(@RequestBody NewLevelDTO newLevelDTO) {
        return levelService.addNewLevel(newLevelDTO);
    }

    @DeleteMapping
    public boolean deleteLastLevelOfType(@RequestParam UpgradeableType type) {
        return levelService.deleteLastLevelOfType(type);
    }
}
