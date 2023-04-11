package com.codecool.spaceship.service;

import com.codecool.spaceship.model.dto.mission.NewMiningMissionDTO;
import com.codecool.spaceship.model.dto.mission.NewScoutingMissionDTO;
import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.dto.mission.MissionDTO;
import com.codecool.spaceship.model.dto.mission.MissionDetailDTO;
import com.codecool.spaceship.model.exception.DataNotFoundException;
import com.codecool.spaceship.model.exception.IllegalOperationException;
import com.codecool.spaceship.model.mission.*;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.ScoutShip;
import com.codecool.spaceship.model.ship.SpaceShip;
import com.codecool.spaceship.repository.LocationRepository;
import com.codecool.spaceship.repository.MissionRepository;
import com.codecool.spaceship.repository.SpaceShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final MissionFactory missionFactory;
    private final SpaceShipRepository spaceShipRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public MissionService(MissionRepository missionRepository, MissionFactory missionFactory, SpaceShipRepository spaceShipRepository, LocationRepository locationRepository) {
        this.missionRepository = missionRepository;
        this.missionFactory = missionFactory;
        this.spaceShipRepository = spaceShipRepository;
        this.locationRepository = locationRepository;
    }

    public List<MissionDTO> getAllActiveMissionsForCurrentUser() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return missionRepository.getMissionsByUserIdAndCurrentStatusNot(user.getId(), MissionStatus.ARCHIVED).stream()
                .map(this::updateAndConvert)
                .toList();
    }

    public List<MissionDTO> getAllActiveMissionsByUserId(Long userId) {
        return missionRepository.getMissionsByUserIdAndCurrentStatusNot(userId, MissionStatus.ARCHIVED).stream()
                .map(this::updateAndConvert)
                .toList();
    }

    public List<MissionDTO> getAllArchivedMissionsForCurrentUser() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return missionRepository.getMissionsByUserIdAndCurrentStatus(user.getId(), MissionStatus.ARCHIVED).stream()
                .map(MissionDTO::new)
                .toList();
    }

    public List<MissionDTO> getAllArchivedMissionsByUserId(Long userId) {
        return missionRepository.getMissionsByUserIdAndCurrentStatus(userId, MissionStatus.ARCHIVED).stream()
                .map(MissionDTO::new)
                .toList();
    }

    public MissionDetailDTO getMissionById(Long id) throws DataNotFoundException {
        Mission mission = getMissionByIdAndCheckAccess(id);
        MissionManager missionManager = missionFactory.getMissionManager(mission);
        if (missionManager.updateStatus()) {
            missionRepository.save(mission);
        }
        return missionManager.getDetailedDTO();
    }

    public MissionDetailDTO startNewMiningMission(NewMiningMissionDTO newMissionDTO) throws DataNotFoundException, IllegalOperationException {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SpaceShip spaceShip = spaceShipRepository.findById(newMissionDTO.shipId())
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d.".formatted(newMissionDTO.shipId())));
        if (!Objects.equals(user.getId(), spaceShip.getUser().getId())) {
            throw new SecurityException("You don't have authority to send this ship.");
        }
        if (!(spaceShip instanceof MinerShip)) {
            throw new IllegalArgumentException("Ship is not a miner ship.");
        }
        Location location = locationRepository.findById(newMissionDTO.locationId())
                .orElseThrow(() -> new DataNotFoundException("No location found with id %d.".formatted(newMissionDTO.locationId())));
        MiningMission mission = missionFactory.startNewMiningMission((MinerShip) spaceShip, location, newMissionDTO.activityTime());
        mission = missionRepository.save(mission);
        MissionManager missionManager = missionFactory.getMissionManager(mission);
        missionManager.updateStatus();
        missionRepository.save(mission);
        return missionManager.getDetailedDTO();
    }

    public MissionDetailDTO startNewScoutingMission(NewScoutingMissionDTO newMissionDTO) {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SpaceShip spaceShip = spaceShipRepository.findById(newMissionDTO.shipId())
                .orElseThrow(() -> new DataNotFoundException("No ship found with id %d.".formatted(newMissionDTO.shipId())));
        if (!Objects.equals(user.getId(), spaceShip.getUser().getId())) {
            throw new SecurityException("You don't have authority to send this ship.");
        }
        if (!(spaceShip instanceof ScoutShip)) {
            throw new IllegalArgumentException("Ship is not a scout ship.");
        }
        ScoutingMission mission = missionFactory.startNewScoutingMission((ScoutShip) spaceShip, newMissionDTO.distance(),
                newMissionDTO.activityTime(), newMissionDTO.targetResource(), newMissionDTO.prioritizingDistance());
        mission = missionRepository.save(mission);
        MissionManager missionManager = missionFactory.getMissionManager(mission);
        missionManager.updateStatus();
        missionRepository.save(mission);
        return missionManager.getDetailedDTO();
    }

    public MissionDetailDTO archiveMission(Long id) throws DataNotFoundException, IllegalOperationException {
        Mission mission = getMissionByIdAndCheckAccess(id);
        MissionManager missionManager = missionFactory.getMissionManager(mission);
        if (missionManager.archiveMission()) {
            missionRepository.save(mission);
            return missionManager.getDetailedDTO();
        }
        return null;
    }

    public MissionDetailDTO abortMission(Long id) throws DataNotFoundException, IllegalOperationException {
        Mission mission = getMissionByIdAndCheckAccess(id);
        MissionManager missionManager = missionFactory.getMissionManager(mission);
        if (missionManager.updateStatus()) {
            missionRepository.save(mission);
        }
        if (missionManager.abortMission()) {
            missionRepository.save(mission);
            return missionManager.getDetailedDTO();
        }
        return null;
    }

    private MissionDTO updateAndConvert(Mission mission) {
        MissionManager missionManager = missionFactory.getMissionManager(mission);
        if (missionManager.updateStatus()) {
            mission = missionRepository.save(mission);
        }
        return new MissionDTO(mission);
    }

    private Mission getMissionByIdAndCheckAccess(Long id) throws DataNotFoundException {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("No mission found with id %d".formatted(id)));
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                || Objects.equals(user.getId(), mission.getUser().getId())) {
            return mission;
        } else {
            throw new SecurityException("You don't have authority to access this mission");
        }
    }

}
