package com.codecool.spaceship.model;

import com.codecool.spaceship.model.exception.UpgradeNotAvailableException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.service.LevelService;

import java.util.Map;

public abstract class Upgradeable {

    private final LevelService levelService;
    protected Level currentLevel;

    protected Upgradeable(LevelService levelService, UpgradeableType type, int level) {
        this.levelService = levelService;
        this.currentLevel = levelService.getLevelByTypeAndLevel(type, level);
    }

    public boolean isFullyUpgraded() {
        return currentLevel.isMax();
    }

    public Map<ResourceType,Integer> getUpgradeCost() throws UpgradeNotAvailableException {
        if (isFullyUpgraded()) {
            throw new UpgradeNotAvailableException("Already at max level");
        } else {
            Integer level = currentLevel.getLevel();
            UpgradeableType type = currentLevel.getType();
            Level nextLevel = levelService.getLevelByTypeAndLevel(type, level + 1);
            return nextLevel.getCost();
        }
    }

    public boolean upgrade() {
        if (isFullyUpgraded()) {
            throw new UpgradeNotAvailableException("Already at max level");
        } else {
            Integer level = currentLevel.getLevel();
            UpgradeableType type = currentLevel.getType();
            currentLevel = levelService.getLevelByTypeAndLevel(type, level + 1);
            System.out.println(currentLevel.getEffect());
            return true;
        }
    }

    public int getCurrentLevel() {
        return currentLevel.getLevel();
    }
}
