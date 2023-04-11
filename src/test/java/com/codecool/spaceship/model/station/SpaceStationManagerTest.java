package com.codecool.spaceship.model.station;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpaceStationManagerTest {

//    @Mock
//    MinerShipService ship;
//    @Mock
//    MinerShipService ship2;
//    @Mock
//    MinerShipService ship3;
//
//    @Test
//    void addNewShipSuccess() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of());
//        try {
//            assertTrue(spaceStationManager.addNewShip(ship));
//        } catch (StorageException ignored) {
//        }
//    }
//
//    @Test
//    void addNewShipNotEnoughResource() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of(ResourceType.METAL, 1));
//        assertThrows(StorageException.class, () -> spaceStationManager.addNewShip(ship));
//    }
//
//    @Test
//    void addNewShipNotEnoughDocks() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of());
//        when(ship2.getCost()).thenReturn(Map.of());
//        when(ship3.getCost()).thenReturn(Map.of());
//        try {
//            spaceStationManager.addNewShip(ship);
//            spaceStationManager.addNewShip(ship2);
//        } catch (StorageException ignored) {
//        }
//        assertThrows(StorageException.class, () -> spaceStationManager.addNewShip(ship3));
//    }
//
//    @Test
//    void addNewShipShipAlreadyAdded() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of());
//        try {
//            spaceStationManager.addNewShip(ship);
//            assertFalse(spaceStationManager.addNewShip(ship));
//        } catch (StorageException ignored) {
//        }
//    }
//
//    @Test
//    void deleteShipSuccess() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of());
//        try {
//            spaceStationManager.addNewShip(ship);
//        } catch (StorageException ignored) {
//        }
//        assertTrue(spaceStationManager.deleteShip(ship));
//    }
//
//    @Test
//    void deleteShipNoSuchShip() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        assertFalse(spaceStationManager.deleteShip(ship));
//    }
//
//    @Test
//    void getAllShipsEmpty() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        Set<SpaceShipService> expected = Set.of();
//        assertEquals(expected, spaceStationManager.getAllShips());
//    }
//
//    @Test
//    void getAllShipsNotEmpty() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of());
//        try {
//            spaceStationManager.addNewShip(ship);
//        } catch (StorageException ignored) {
//        }
//        Set<SpaceShipService> expected = Set.of(ship);
//        assertEquals(expected, spaceStationManager.getAllShips());
//    }
//
//    @Mock
//    Drill drill;
//
//    @Test
//    void upgradeShipPartSuccess() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        try {
//            when(ship.getCost()).thenReturn(Map.of());
//            spaceStationManager.addNewShip(ship);
//            when(ship.isAvailable()).thenReturn(true);
//            when(ship.getPart(ShipPart.DRILL)).thenReturn(drill);
//            when(drill.getUpgradeCost()).thenReturn(Map.of());
//        } catch (Exception ignored) {
//        }
//        try {
//            assertTrue(spaceStationManager.upgradeShipPart(ship, ShipPart.DRILL));
//        } catch (Exception ignored) {
//        }
//        verify(drill, times(1)).upgrade();
//    }
//
//    @Test
//    void upgradeShipPartShipNotInStation() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        assertThrows(StorageException.class, () -> spaceStationManager.upgradeShipPart(ship, ShipPart.DRILL));
//    }
//
//    @Test
//    void upgradeShipPartShipNotAvailable() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of());
//        when(ship.isAvailable()).thenReturn(false);
//        try {
//            spaceStationManager.addNewShip(ship);
//        } catch (Exception ignored) {
//        }
//        assertThrows(UpgradeNotAvailableException.class, () -> spaceStationManager.upgradeShipPart(ship, ShipPart.DRILL));
//    }
//
//    @Test
//    void upgradeShipPartNotEnoughResource() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        when(ship.getCost()).thenReturn(Map.of());
//        when(ship.isAvailable()).thenReturn(true);
//        try {
//            spaceStationManager.addNewShip(ship);
//            when(ship.getPart(ShipPart.DRILL)).thenReturn(drill);
//            when(drill.getUpgradeCost()).thenReturn(Map.of(ResourceType.METAL, 1));
//        } catch (Exception ignored) {
//        }
//        assertThrows(StorageException.class, () -> spaceStationManager.upgradeShipPart(ship, ShipPart.DRILL));
//    }
//
//    @Test
//    void addResourceSuccess() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        try {
//            assertTrue(spaceStationManager.addResource(ResourceType.METAL, 1));
//        } catch (Exception ignored) {
//        }
//    }
//
//    @Test
//    void addResourceNotEnoughSpace() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        assertThrows(StorageException.class, () -> spaceStationManager.addResource(ResourceType.METAL, 21));
//    }
//
//    @Test
//    void upgradeStorageSuccess() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        try {
//            spaceStationManager.addResource(ResourceType.METAL, 5);
//        } catch (Exception ignored) {
//        }
//        assertDoesNotThrow(spaceStationManager::upgradeStorage);
//    }
//
//    @Test
//    void upgradeStorageNotEnoughResource() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        assertThrows(StorageException.class, spaceStationManager::upgradeStorage);
//    }
//
//    @Test
//    void upgradeHangarSuccess() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        try {
//            spaceStationManager.addResource(ResourceType.METAL, 5);
//        } catch (Exception ignored) {
//        }
//        assertDoesNotThrow(spaceStationManager::upgradeHangar);
//    }
//
//    @Test
//    void upgradeHangarNotEnoughResource() {
//        SpaceStationManager spaceStationManager = new SpaceStationManager("test");
//        assertThrows(StorageException.class, spaceStationManager::upgradeHangar);
//    }
}