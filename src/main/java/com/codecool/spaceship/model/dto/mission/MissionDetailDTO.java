package com.codecool.spaceship.model.dto.mission;

import com.codecool.spaceship.model.mission.Event;
import com.codecool.spaceship.model.mission.MissionStatus;
import com.codecool.spaceship.model.mission.MissionType;
import com.codecool.spaceship.model.ship.SpaceShip;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class MissionDetailDTO extends MissionDTO {

    private final List<EventDTO> reports;
    private final long shipId;
    private final String shipName;

    public MissionDetailDTO(long id, String title, MissionType missionType, MissionStatus status, LocalDateTime currentObjectiveTime,
                            LocalDateTime approxEndTime, List<Event> events, SpaceShip ship) {
        super(id, title, missionType, status, currentObjectiveTime, approxEndTime);
        reports = formatEventLog(events);
        shipId = ship.getId();
        shipName = ship.getName();
    }

    private static List<EventDTO> formatEventLog(List<Event> events) {
        return events.stream()
                .filter(event -> Objects.nonNull(event.getEventMessage()))
                .map(event -> new EventDTO(event.getEndTime(), event.getEventMessage()))
                .toList();
    }
}
