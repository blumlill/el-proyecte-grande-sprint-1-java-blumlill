package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.exception.StorageException;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MiningMissionManagerTest {

    @Mock
    MinerShip minerShipMock;
    @Mock
    MinerShipManager minerShipManagerMock;
    @Mock
    Location locationMock;
    @Mock
    Event eventMock;
    @Mock
    Random randomMock;

    @Test
    void missionCreationTest() throws IllegalOperationException {
        when(minerShipManagerMock.isAvailable()).thenReturn(true);
        when(minerShipManagerMock.getSpeed()).thenReturn(2);
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(locationMock.getDistanceFromStation()).thenReturn(5);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        MiningMission expected = MiningMission.builder()
                .startTime(now)
                .currentObjectiveTime(now.plusSeconds(9000L))
                .approxEndTime(now.plusSeconds(19800L))
                .currentStatus(MissionStatus.EN_ROUTE)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(9000L)
                .activityDurationInSecs(1800L)
                .events(new ArrayList<>())
                .build();

        MiningMission actual = MiningMissionManager.startMiningMission(minerShipManagerMock, locationMock, 1800L, clock);

        assertEquals(expected, actual);
    }

    @Test
    void noUpdateStatusBeforeNextUpdateTest() {
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(eventMock.getEndTime()).thenReturn(LocalDateTime.now().plusSeconds(1));

        MiningMission mission = MiningMission.builder()
                .ship(minerShipMock)
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);

        MiningMissionManager missionManager = new MiningMissionManager(mission, minerShipManagerMock);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }

    @Test
    void noUpdateStatusWhenMissionIsOverTest() {
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        MiningMission mission = MiningMission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.OVER)
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);

        MiningMissionManager missionManager = new MiningMissionManager(mission, minerShipManagerMock);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }

    @Test
    void updateStatusAfterStartTest() {
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        MiningMission expected = MiningMission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new ArrayList<>())
                .build();
        Event event1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(5))
                .build();
        expected.getEvents().add(event1);
        Event event2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.plusSeconds(5))
                .build();
        expected.getEvents().add(event2);

        MiningMission actual = MiningMission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.plusSeconds(5))
                .events(new ArrayList<>())
                .build();
        actual.getEvents().add(event1);

        MiningMissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithTimeToFillTest() {
        int emptySpace = 8;
        int minedPerHour = 5;
        long originalActivityDuration = TimeUnit.HOURS.toSeconds(2);
        long updatedActivityDuration = (long) Math.floor(((double) emptySpace / minedPerHour) * 60 * 60);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(minedPerHour);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(emptySpace);
        when(locationMock.getResourceReserve()).thenReturn(100);

        MiningMission expected = MiningMission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(originalActivityDuration - 5))
                .activityDurationInSecs(updatedActivityDuration)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.plusSeconds(updatedActivityDuration - 5))
                .build();
        expected.getEvents().add(expectedEvent2);

        MiningMission actual = MiningMission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(originalActivityDuration)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithNoTimeToFillTest() {
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);
        long activityDuration = TimeUnit.HOURS.toSeconds(2);

        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8);
        when(locationMock.getResourceReserve()).thenReturn(100);

        MiningMission expected = MiningMission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(activityDuration - 5))
                .activityDurationInSecs(activityDuration)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.plusSeconds(activityDuration - 5))
                .build();
        expected.getEvents().add(expectedEvent2);

        MiningMission actual = MiningMission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(activityDuration)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MiningMissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void updateStatusArriveAtLocationWithLowResources() {
        int emptySpace = 10;
        int resourceReserve = 5;
        long originalActivityDuration = TimeUnit.HOURS.toSeconds(emptySpace);
        long updatedActivityDuration = originalActivityDuration / (emptySpace / resourceReserve);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        when(locationMock.getName()).thenReturn("Test Planet");
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(emptySpace);
        when(locationMock.getResourceReserve()).thenReturn(resourceReserve);

        MiningMission expected = MiningMission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.plusSeconds(originalActivityDuration - 5))
                .activityDurationInSecs(updatedActivityDuration)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.plusSeconds(updatedActivityDuration - 5))
                .build();
        expected.getEvents().add(expectedEvent2);

        MiningMission actual = MiningMission.builder()
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(originalActivityDuration)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithTimeToFillTest() throws StorageException {
        int emptySpace = 8;
        int minedPerHour = 5;
        long activityDuration = (long) (((double)emptySpace / minedPerHour) * 60 * 60);
        long travelTime = TimeUnit.HOURS.toSeconds(2);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(minedPerHour);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(0);
        when(locationMock.getResourceReserve()).thenReturn(100);

        MiningMission expected = MiningMission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(travelTime - 5))
                .activityDurationInSecs(activityDuration)
                .approxEndTime(now.plusSeconds(travelTime - 5))
                .travelDurationInSecs(travelTime)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.minusSeconds(5))
                .eventMessage("Storage is full. Mined 8 CRYSTAL(s). Returning to station.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(travelTime - 5))
                .build();
        expected.getEvents().add(expectedEvent2);

        MiningMission actual = MiningMission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(activityDuration)
                .travelDurationInSecs(travelTime)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MiningMissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(locationMock, times(1)).setResourceReserve(92);
        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithNoTimeToFillTest() throws StorageException {
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(1);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(8, 6);
        when(locationMock.getResourceReserve()).thenReturn(100);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        MiningMission expected = MiningMission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(7196))
                .activityDurationInSecs(7200L)
                .travelDurationInSecs(7200L)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.minusSeconds(4))
                .eventMessage("Mining complete. Mined 2 CRYSTAL(s). Returning to station.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(7196))
                .build();
        expected.getEvents().add(expectedEvent2);

        MiningMission actual = MiningMission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(4))
                .activityDurationInSecs(7200L)
                .travelDurationInSecs(7200L)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.minusSeconds(4))
                .build();
        actual.getEvents().add(actualEvent);

        MiningMissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(locationMock, times(1)).setResourceReserve(98);
        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 2);
        assertEquals(expected, actual);
    }

    @Test
    void finishMiningWithDepletingPlanet() throws StorageException {
        int reserve = 5;
        int minedPerHour = 5;
        long activityTime = (long) (((double)reserve / minedPerHour) * 60 * 60);
        long travelTime = TimeUnit.HOURS.toSeconds(2);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(minedPerHour);
        when(locationMock.getResourceReserve()).thenReturn(reserve,0);

        MiningMission expected = MiningMission.builder()
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.plusSeconds(travelTime - 5))
                .activityDurationInSecs(activityTime)
                .approxEndTime(now.plusSeconds(travelTime - 5))
                .travelDurationInSecs(travelTime)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.minusSeconds(5))
                .eventMessage("Planet depleted. Mined 5 CRYSTAL(s). Returning to station.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.plusSeconds(travelTime - 5))
                .build();
        expected.getEvents().add(expectedEvent2);

        MiningMission actual = MiningMission.builder()
                .currentStatus(MissionStatus.IN_PROGRESS)
                .currentObjectiveTime(now.minusSeconds(5))
                .activityDurationInSecs(activityTime)
                .travelDurationInSecs(travelTime)
                .ship(minerShipMock)
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MiningMissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(locationMock, times(1)).setResourceReserve(0);
        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 5);
        assertEquals(expected, actual);
    }



    @Test
    void updateStatusMissionEndTest() {
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        MiningMission expected = MiningMission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.OVER)
                .currentObjectiveTime(now.minusSeconds(5))
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .eventMessage("Returned to station.")
                .build();
        expected.getEvents().add(expectedEvent);

        MiningMission actual = MiningMission.builder()
                .ship(minerShipMock)
                .currentStatus(MissionStatus.RETURNING)
                .currentObjectiveTime(now.minusSeconds(5))
                .location(locationMock)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .build();
        actual.getEvents().add(actualEvent);

        MiningMissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        assertEquals(expected, actual);
        verify(locationMock, times(1)).setCurrentMission(null);
    }

    @Test
    void updateStatusStartToFinishTest() throws StorageException {
        when(locationMock.getName()).thenReturn("Test Planet");
        when(locationMock.getResourceType()).thenReturn(ResourceType.CRYSTAL);
        when(minerShipManagerMock.getShip()).thenReturn(minerShipMock);
        when(minerShipManagerMock.getDrillEfficiency()).thenReturn(5);
        when(minerShipManagerMock.getEmptyStorageSpace()).thenReturn(20, 20, 12);
        when(locationMock.getResourceReserve()).thenReturn(100);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        MiningMission expected = MiningMission.builder()
                .startTime(now.minusSeconds(20000L))
                .currentObjectiveTime(now.minusSeconds(2900L))
                .currentStatus(MissionStatus.OVER)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(5400L)
                .activityDurationInSecs(6300L)
                .events(new ArrayList<>())
                .build();
        Event expectedEvent1 = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(20000L))
                .eventMessage("Left station for mining mission on Test planet.")
                .build();
        expected.getEvents().add(expectedEvent1);
        Event expectedEvent2 = Event.builder()
                .eventType(EventType.ARRIVAL_AT_LOCATION)
                .endTime(now.minusSeconds(14600L))
                .eventMessage("Arrived on Test Planet. Starting mining operation.")
                .build();
        expected.getEvents().add(expectedEvent2);
        Event expectedEvent3 = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(now.minusSeconds(8300L))
                .eventMessage("Mining complete. Mined 8 CRYSTAL(s). Returning to station.")
                .build();
        expected.getEvents().add(expectedEvent3);
        Event expectedEvent4 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(2900L))
                .eventMessage("Returned to station.")
                .build();
        expected.getEvents().add(expectedEvent4);


        MiningMission actual = MiningMission.builder()
                .startTime(now.minusSeconds(20000L))
                .currentObjectiveTime(now.minusSeconds(14600L))
                .currentStatus(MissionStatus.EN_ROUTE)
                .location(locationMock)
                .ship(minerShipMock)
                .travelDurationInSecs(5400L)
                .activityDurationInSecs(6300L)
                .events(new ArrayList<>())
                .build();
        Event actualEvent = Event.builder()
                .eventType(EventType.START)
                .endTime(now.minusSeconds(20000L))
                .eventMessage("Left station for mining mission on Test planet.")
                .build();
        actual.getEvents().add(actualEvent);

        MiningMissionManager missionManager = new MiningMissionManager(actual, clock, randomMock, minerShipManagerMock);
        missionManager.setShipManager(minerShipManagerMock);
        missionManager.updateStatus();

        verify(minerShipManagerMock, times(1)).addResourceToStorage(ResourceType.CRYSTAL, 8);
        assertEquals(expected, actual);
    }

}