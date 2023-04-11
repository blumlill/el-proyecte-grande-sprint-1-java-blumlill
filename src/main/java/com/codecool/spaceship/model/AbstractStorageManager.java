package com.codecool.spaceship.model;

import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.service.LevelService;

import java.util.Map;

public class AbstractStorageManager extends Upgradeable {

    protected Map<ResourceType, Integer> storedResources;

    protected AbstractStorageManager(LevelService levelService, UpgradeableType type, int level, Map<ResourceType, Integer> storedResources) {
        super(levelService, type, level);
        if (storedResources.values().stream().mapToInt(i -> i).sum() > super.currentLevel.getEffect()) {
            throw new StorageException("Stored resources can't exceed %d at this level".formatted(super.currentLevel.getEffect()));
        }
        this.storedResources = storedResources;
    }

    public int getCurrentCapacity() {
        return currentLevel.getEffect();
    }

    public int getCurrentAvailableStorageSpace() {
        return getCurrentCapacity() - storedResources.values().stream().mapToInt(i -> i).sum();
    }

    public Map<ResourceType, Integer> getStoredResources() {
        return storedResources;
    }

    public boolean addResource(ResourceType resourceType, int quantity) throws StorageException {
        if (quantity < 0) {
            throw new StorageException("Can't add negative resources.");
        }
        if (quantity > getCurrentAvailableStorageSpace()) {
            throw new StorageException("Not enough storage space.");
        }
        storedResources.merge(resourceType, quantity, Integer::sum);
        return true;
    }

    public boolean hasResource(ResourceType resourceType, int quantity) {
        Integer storedAmount = storedResources.get(resourceType);
        return storedAmount != null && storedAmount >= quantity;
    }

    public boolean removeResource(ResourceType resourceType, int quantity) throws StorageException {
        if (quantity < 0) {
            throw new StorageException("Can't remove negative resources.");
        }
        if (!hasResource(resourceType, quantity)) {
            throw new StorageException("Not enough resource.");
        }
        int newValue = storedResources.get(resourceType) - quantity;
        storedResources.replace(resourceType, newValue);
        return true;
    }
}
