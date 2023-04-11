package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.service.LevelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpaceStationManagerStorageTest {

    @Mock
    LevelService levelServiceMock;
    @Mock
    Level levelMock;

    @Test
    void getCurrentCapacityTestLevel1() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
        int expected = 20;

        assertEquals(expected, stationStorageManager.getCurrentCapacity());
    }

    @Test
    void getCurrentCapacityTestLevel3() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 3)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(100);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock, 3, new HashMap<>());
        int expected = 100;
        assertEquals(expected, stationStorageManager.getCurrentCapacity());
    }

    @Test
    void addResourceSuccessOne() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
        stationStorageManager.addResource(ResourceType.METAL, 5);

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 5);
        }};
        assertEquals(expected, stationStorageManager.getStoredResources());
    }

    @Test
    void addResourceSuccessMultiple() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
        stationStorageManager.addResource(ResourceType.METAL, 5);
        stationStorageManager.addResource(ResourceType.METAL, 10);
        stationStorageManager.addResource(ResourceType.SILICONE, 2);

        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 15);
            put(ResourceType.SILICONE, 2);
        }};
        assertEquals(expected, stationStorageManager.getStoredResources());
    }

    @Test
    void addResourceError() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
        assertThrows(StorageException.class, () -> stationStorageManager.addResource(ResourceType.METAL, 25));
    }

    @Test
    void hasResourceTrue() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
        stationStorageManager.addResource(ResourceType.METAL, 5);
        assertTrue(stationStorageManager.hasResource(ResourceType.METAL, 5));
    }

    @Test
    void hasResourceFalse() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
        assertFalse(stationStorageManager.hasResource(ResourceType.METAL, 5));
    }

    @Test
    void removeResourceSuccess() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
            stationStorageManager.addResource(ResourceType.METAL, 5);
            stationStorageManager.removeResource(ResourceType.METAL, 4);
        Map<ResourceType, Integer> expected = new HashMap<>() {{
            put(ResourceType.METAL, 1);
        }};
        assertEquals(expected, stationStorageManager.getStoredResources());
    }

    @Test
    void removeResourceError() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.STATION_STORAGE, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(20);
        StationStorageManager stationStorageManager = new StationStorageManager(levelServiceMock);
        assertThrows(StorageException.class, () -> stationStorageManager.removeResource(ResourceType.METAL, 1));
    }

//    Upgrading is no longer implemented in the manager classes, test elsewhere
//
//    @Test
//    void getUpgradeCostSuccess() {
//        StationStorageManager stationStorageManager = new StationStorageManager(levelService);
//        Map<ResourceType, Integer> actual = null;
//        try {
//            actual = stationStorageManager.getUpgradeCost();
//        } catch (UpgradeNotAvailableException ignored) {
//        }
//
//        Map<ResourceType, Integer> expected = new HashMap<>() {{
//            put(ResourceType.METAL, 5);
//        }};
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getUpgradeCostError() {
//        StationStorageManager stationStorageManager = new StationStorageManager(levelService, 5, new HashMap<>());
//        assertThrows(UpgradeNotAvailableException.class, stationStorageManager::getUpgradeCost);
//    }
//
//    @Test
//    void upgradeSuccess() {
//        StationStorageManager stationStorageManager = new StationStorageManager(levelService);
//        stationStorageManager.upgrade();
//        int expected = 2;
//        assertEquals(expected, stationStorageManager.getCurrentLevel());
//    }
//
//    @Test
//    void upgradeMaxLevelCannotBeExceeded() {
//        StationStorageManager stationStorageManager = new StationStorageManager(levelService, 5, new HashMap<>());
//        int expected = 5;
//        assertEquals(expected, stationStorageManager.getCurrentLevel());
//    }
}