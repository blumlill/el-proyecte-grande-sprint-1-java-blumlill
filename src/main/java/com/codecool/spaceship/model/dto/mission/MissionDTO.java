package com.codecool.spaceship.model.dto.mission;

import com.codecool.spaceship.model.mission.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MissionDTO {

    private final long id;
    private final String title;
    private final MissionType type;
    private final MissionStatus status;
    private final LocalDateTime currentObjectiveTime;
    private final LocalDateTime approxEndTime;

    public MissionDTO(Mission mission) {
        this(mission.getId(), generateTitle(mission), getMissionType(mission), mission.getCurrentStatus(),
                mission.getCurrentObjectiveTime(), mission.getApproxEndTime());
    }

    private static String generateTitle(Mission mission) {
        if (mission instanceof MiningMission) {
            return "Mining mission on %s".formatted(((MiningMission) mission).getLocation().getName());
        } else if (mission instanceof ScoutingMission) {
            return "Exploration mission for %s".formatted(((ScoutingMission)mission).getTargetResource().toString().toLowerCase());
        } else {
            throw new RuntimeException("Mission type not recognized");
        }
    }

    private static MissionType getMissionType(Mission mission) {
        if (mission instanceof MiningMission) {
            return MissionType.MINING;
        } else if (mission instanceof ScoutingMission) {
            return MissionType.SCOUTING;
        } else {
            throw new RuntimeException("Unrecognized mission type");
        }
    }
}
