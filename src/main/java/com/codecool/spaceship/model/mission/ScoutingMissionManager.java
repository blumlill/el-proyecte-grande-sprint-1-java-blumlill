package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.dto.mission.MissionDetailDTO;
import com.codecool.spaceship.model.dto.mission.ScoutingMissionDTO;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.location.LocationDataGenerator;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.ScoutShipManager;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class ScoutingMissionManager extends MissionManager {

    private final LocationDataGenerator locationDataGenerator;

    public ScoutingMissionManager(ScoutingMission mission, Clock clock, Random random, ScoutShipManager scoutShipManager, LocationDataGenerator locationDataGenerator) {
        super(mission, clock, random, scoutShipManager);
        this.locationDataGenerator = locationDataGenerator;
    }

    public ScoutingMissionManager(ScoutingMission mission, ScoutShipManager scoutShipManager, LocationDataGenerator locationDataGenerator) {
        super(mission, Clock.systemUTC(), new Random(), scoutShipManager);
        this.locationDataGenerator = locationDataGenerator;
    }

    public static ScoutingMission startScoutingMission(ScoutShipManager scoutShipManager, int distance, long activityDurationInSecs,
                                               ResourceType targetResource, boolean prioritizingDistance, Clock clock) throws IllegalOperationException {
        if (!scoutShipManager.isAvailable()) {
            throw new IllegalOperationException("This ship is already on a mission");
        }
        if (distance <= 0) {
            throw new IllegalArgumentException("Distance can't be 0 or less");
        }
        if (activityDurationInSecs <= 0) {
            throw new IllegalArgumentException("Activity duration can't be 0 or less");
        }
        if (targetResource == null) {
            throw new IllegalArgumentException("Target resource can't be null");
        }
        LocalDateTime startTime = LocalDateTime.now(clock);
        long travelDurationInSecs = calculateTravelDurationInSecs(scoutShipManager, distance);
        long approxMissionDurationInSecs = travelDurationInSecs * 2 + activityDurationInSecs;

        ScoutingMission mission = ScoutingMission.builder()
                .startTime(startTime)
                .activityDurationInSecs(activityDurationInSecs)
                .travelDurationInSecs(travelDurationInSecs)
                .currentStatus(MissionStatus.EN_ROUTE)
                .currentObjectiveTime(startTime.plusSeconds(travelDurationInSecs))
                .approxEndTime(startTime.plusSeconds(approxMissionDurationInSecs))
                .ship(scoutShipManager.getShip())
                .user(scoutShipManager.getShip().getUser())
                .events(new ArrayList<>())
                .targetResource(targetResource)
                .distance(distance)
                .prioritizingDistance(prioritizingDistance)
                .build();
        scoutShipManager.setCurrentMission(mission);
        return mission;
    }

    public static ScoutingMission startScoutingMission(ScoutShipManager scoutShipManager, int distance, long activityDurationInSecs,
                                               ResourceType targetResource, boolean prioritizingDistance) throws IllegalOperationException {
        return startScoutingMission(scoutShipManager, distance, activityDurationInSecs, targetResource, prioritizingDistance, Clock.systemUTC());
    }

    @Override
    public MissionDetailDTO getDetailedDTO() {
        return new ScoutingMissionDTO((ScoutingMission) mission);
    }

    @Override
    public boolean abortMission() throws IllegalOperationException {
        switch (mission.getCurrentStatus()) {
            case OVER, ARCHIVED -> throw new IllegalOperationException("Mission is already over.");
            case RETURNING -> throw new IllegalOperationException("Mission is already returning.");
        }
        LocalDateTime now = LocalDateTime.now(clock);
        Event abortedEvent = popLastEvent();
        Event abortEvent = Event.builder()
                .eventType(EventType.ABORT)
                .endTime(now)
                .build();

        if (abortedEvent.getEventType() == EventType.ACTIVITY_COMPLETE) {
            long timeScanning = Duration.between(peekLastEvent().getEndTime(), now).getSeconds();
            mission.setActivityDurationInSecs(timeScanning);
            abortEvent.setEventMessage("Mission aborted by Command. Shutting down scan. Returning to station.");
        } else {
            mission.setActivityDurationInSecs(0);
            abortEvent.setEventMessage("Mission aborted by Command. Returning to station.");
        }

        pushNewEvent(abortEvent);
        mission.setApproxEndTime(now.plusSeconds(mission.getTravelDurationInSecs()));
        startReturnTravel();
        return true;
    }

    @Override
    protected void addStartEvent() {
        Event startEvent = Event.builder()
                .endTime(mission.getStartTime())
                .eventType(EventType.START)
                .eventMessage("Left station to discover new planet with %s.".formatted(((ScoutingMission) mission).getTargetResource()))
                .build();
        pushNewEvent(startEvent);
    }

    @Override
    protected void startActivity() {
        mission.setCurrentStatus(MissionStatus.IN_PROGRESS);

        LocalDateTime lastEventTime = peekLastEvent().getEndTime();
        mission.setCurrentObjectiveTime(lastEventTime.plusSeconds(mission.getActivityDurationInSecs()));
        peekLastEvent().setEventMessage("Reached target distance. Starting scan.");
        Event activityEvent = Event.builder()
                .eventType(EventType.ACTIVITY_COMPLETE)
                .endTime(lastEventTime.plusSeconds(mission.getActivityDurationInSecs()))
                .build();
        pushNewEvent(activityEvent);
    }

    @Override
    protected void finishActivity() {
        peekLastEvent().setEventMessage("Finished scanning. Returning to station.");
        startReturnTravel();
    }

    @Override
    protected void endMission() {
        if (mission.getActivityDurationInSecs() == 0 || shipManager.getShieldEnergy() == 0) {
            peekLastEvent().setEventMessage("Returned to station.");
        } else {
            Location location = generateLocation();
            if (location == null) {
                peekLastEvent().setEventMessage("Returned to station.\nNo new planet discovered.");
            } else  {
                ((ScoutingMission)mission).setDiscoveredLocation(location);
                peekLastEvent().setEventMessage("Returned to station.\nScan data decoded, discovered new planet: %s".formatted(location.getName()));
            }
        }
        mission.setCurrentStatus(MissionStatus.OVER);
        shipManager.endMission();
    }

    private Location generateLocation() {
        int scannerEfficiency = ((ScoutShipManager) shipManager).getScannerEfficiency();
        double scanningHours = mission.getActivityDurationInSecs() / 60.0 / 60.0;
        int distance = ((ScoutingMission) mission).getDistance();
        if (mission.getActivityDurationInSecs() == 0
                || !locationDataGenerator.determinePlanetFound(scannerEfficiency, scanningHours, distance)) {
            return null;
        }
        boolean prioritizingDistance = ((ScoutingMission) mission).isPrioritizingDistance();
        int distanceFromStation = locationDataGenerator.determineDistance(scannerEfficiency, distance, prioritizingDistance);
        int resourceReserves = locationDataGenerator.determineResourceReserves(scannerEfficiency, scanningHours, !prioritizingDistance);
        return Location.builder()
                .name(locationDataGenerator.determineName())
                .distanceFromStation(distanceFromStation)
                .discovered(peekLastEvent().getEndTime())
                .resourceType(((ScoutingMission) mission).getTargetResource())
                .resourceReserve(resourceReserves)
                .user(mission.getUser())
                .build();
    }
}
