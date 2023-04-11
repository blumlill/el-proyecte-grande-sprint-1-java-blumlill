package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.Level;
import com.codecool.spaceship.model.UpgradeableType;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.service.LevelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HangarManagerTest {
    @Mock
    MinerShip ship1;
    @Mock
    MinerShip ship2;
    @Mock
    SpaceShip ship3;
    @Mock
    LevelService levelServiceMock;
    @Mock
    Level levelMock;

    @Test
    void addShipSuccess() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.HANGAR, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(2);
        HangarManager hangarManager = new HangarManager(levelServiceMock);
        hangarManager.addShip(ship1);

        assertEquals(Set.of(ship1), hangarManager.getAllShips());
    }

    @Test
    void addShipError() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.HANGAR, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(2);
        HangarManager hangarManager = new HangarManager(levelServiceMock);
        try {
            hangarManager.addShip(ship1);
            hangarManager.addShip(ship2);
        } catch (Exception ignored) {
        }
        assertThrows(StorageException.class, () -> hangarManager.addShip(ship3));
    }

    @Test
    void getAllShipsTwo() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.HANGAR, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(2);
        HangarManager hangarManager = new HangarManager(levelServiceMock);
        try {
            hangarManager.addShip(ship1);
            hangarManager.addShip(ship3);
        } catch (StorageException ignored) {
        }
        Set<SpaceShip> expected = Set.of(ship1, ship3);
        assertEquals(expected, hangarManager.getAllShips());
    }

    @Test
    void getAllShipsEmpty() {
        when(levelServiceMock.getLevelByTypeAndLevel(UpgradeableType.HANGAR, 1)).thenReturn(levelMock);
        when(levelMock.getEffect()).thenReturn(2);
        HangarManager hangarManager = new HangarManager(levelServiceMock);
        Set<SpaceShip> expected = Set.of();
        assertEquals(expected, hangarManager.getAllShips());
    }

//    Upgrading is no longer implemented in the manager classes, test elsewhere
//
//    @Test
//    void getUpgradeCostLevel1() {
//        HangarManager hangarManager = new HangarManager(levelService);
//        Map<ResourceType, Integer> expected = new HashMap<>() {{
//            put(ResourceType.METAL, 5);
//        }};
//        Map<ResourceType, Integer> actual = null;
//        try {
//            actual = hangarManager.getUpgradeCost();
//        } catch (UpgradeNotAvailableException ignored) {
//        }
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getUpgradeCostLevel3() {
//        HangarManager hangarManager = new HangarManager(levelService, 3, new HashSet<>());
//        Map<ResourceType, Integer> expected = new HashMap<>() {{
//            put(ResourceType.METAL, 100);
//            put(ResourceType.SILICONE, 100);
//            put(ResourceType.CRYSTAL, 50);
//        }};
//        Map<ResourceType, Integer> actual = null;
//        try {
//            actual = hangarManager.getUpgradeCost();
//        } catch (UpgradeNotAvailableException ignored) {
//        }
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void getUpgradeCostThrows() {
//        HangarManager hangarManager = new HangarManager(levelService, 5, new HashSet<>());
//        assertThrows(UpgradeNotAvailableException.class, hangarManager::getUpgradeCost);
//    }
//
//    @Test
//    void upgradeSuccess() {
//        HangarManager hangarManager = new HangarManager(levelService);
//        hangarManager.upgrade();
//        int expected = 2;
//        assertEquals(expected, hangarManager.getCurrentLevel());
//    }
//
//    @Test
//    void upgradeMaxLevelExceeded() {
//        HangarManager hangarManager = new HangarManager(levelService, 5, new HashSet<>());
//        hangarManager.upgrade();
//        int expected = 5;
//        assertEquals(expected, hangarManager.getCurrentLevel());
//    }
}