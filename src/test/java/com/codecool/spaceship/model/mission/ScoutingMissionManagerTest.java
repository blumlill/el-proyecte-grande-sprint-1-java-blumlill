package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.location.LocationDataGenerator;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.ScoutShip;
import com.codecool.spaceship.model.ship.ScoutShipManager;
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

class ScoutingMissionManagerTest {

    @Mock
    ScoutShip scoutShipMock;
    @Mock
    ScoutShipManager scoutShipManagerMock;
    @Mock
    LocationDataGenerator locationDataGeneratorMock;
    @Mock
    Event eventMock;

    @Test
    void missionCreationTest() throws IllegalOperationException {
        int speed = 2;
        int distance = 10;
        long activityDuration = TimeUnit.HOURS.toSeconds(3);
        ResourceType targetResource = ResourceType.CRYSTAL;
        boolean prioritizeDistance = false;
        long travelTime = TimeUnit.HOURS.toSeconds(distance) / speed;
        when(scoutShipManagerMock.isAvailable()).thenReturn(true);
        when(scoutShipManagerMock.getSpeed()).thenReturn(speed);
        when(scoutShipManagerMock.getShip()).thenReturn(scoutShipMock);
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        ScoutingMission expected = ScoutingMission.builder()
                .startTime(now)
                .currentObjectiveTime(now.plusSeconds(travelTime))
                .approxEndTime(now.plusSeconds(travelTime * 2 + activityDuration))
                .currentStatus(MissionStatus.EN_ROUTE)
                .ship(scoutShipMock)
                .travelDurationInSecs(travelTime)
                .activityDurationInSecs(activityDuration)
                .events(new ArrayList<>())
                .prioritizingDistance(prioritizeDistance)
                .targetResource(targetResource)
                .distance(distance)
                .build();

        ScoutingMission actual = ScoutingMissionManager.startScoutingMission(scoutShipManagerMock, distance,
                activityDuration, targetResource, prioritizeDistance, clock);

        assertEquals(expected, actual);
    }

    @Test
    void noUpdateStatusBeforeNextUpdateTest() {
        when(scoutShipManagerMock.getShip()).thenReturn(scoutShipMock);
        when(eventMock.getEndTime()).thenReturn(LocalDateTime.now().plusSeconds(1));

        ScoutingMission mission = ScoutingMission.builder()
                .ship(scoutShipMock)
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);

        ScoutingMissionManager missionManager = new ScoutingMissionManager(mission, scoutShipManagerMock, locationDataGeneratorMock);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }

    @Test
    void noUpdateStatusWhenMissionIsOverTest() {
        when(scoutShipManagerMock.getShip()).thenReturn(scoutShipMock);
        ScoutingMission mission = ScoutingMission.builder()
                .ship(scoutShipMock)
                .currentStatus(MissionStatus.OVER)
                .events(new ArrayList<>())
                .build();
        mission.getEvents().add(eventMock);

        ScoutingMissionManager missionManager = new ScoutingMissionManager(mission, scoutShipManagerMock, locationDataGeneratorMock);
        missionManager.updateStatus();

        verify(eventMock, never()).getEventType();
    }


    @Test
    void TestGeneratesPlanetCorrectly() {
        when(scoutShipManagerMock.getShip()).thenReturn(scoutShipMock);
        when(scoutShipManagerMock.getShieldEnergy()).thenReturn(100);
        when(scoutShipManagerMock.getScannerEfficiency()).thenReturn(1);
        when(locationDataGeneratorMock.determinePlanetFound(1, 2.0, 10)).thenReturn(true);
        when(locationDataGeneratorMock.determineDistance(1, 10, true)).thenReturn(10);
        when(locationDataGeneratorMock.determineResourceReserves(1, 2.0, false)).thenReturn(50);
        when(locationDataGeneratorMock.determineName()).thenReturn("Test");
        Clock clock = Clock.fixed(Instant.parse("2022-08-15T09:00:00.00Z"), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now(clock);

        Location expected = Location.builder()
                .name("Test")
                .resourceReserve(50)
                .distanceFromStation(10)
                .resourceType(ResourceType.CRYSTAL)
                .discovered(now.minusSeconds(5))
                .build();

        ScoutingMission mission = ScoutingMission.builder()
                .ship(scoutShipMock)
                .prioritizingDistance(true)
                .activityDurationInSecs(2 * 60 * 60)
                .targetResource(ResourceType.CRYSTAL)
                .distance(10)
                .events(new ArrayList<>())
                .build();
        Event event1 = Event.builder()
                .eventType(EventType.RETURNED_TO_STATION)
                .endTime(now.minusSeconds(5))
                .build();
        mission.getEvents().add(event1);

        ScoutingMissionManager missionManager = new ScoutingMissionManager(mission, clock, new Random(), scoutShipManagerMock, locationDataGeneratorMock);
        missionManager.updateStatus();

        Location actual = mission.getDiscoveredLocation();
        assertEquals(expected, actual);
    }
}