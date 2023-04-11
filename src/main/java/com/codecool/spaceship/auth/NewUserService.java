package com.codecool.spaceship.auth;

import com.codecool.spaceship.model.Role;
import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.location.LocationDataGenerator;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.MinerShip;
import com.codecool.spaceship.model.ship.MinerShipManager;
import com.codecool.spaceship.model.ship.shipparts.Color;
import com.codecool.spaceship.model.station.SpaceStation;
import com.codecool.spaceship.model.station.SpaceStationManager;
import com.codecool.spaceship.repository.LocationRepository;
import com.codecool.spaceship.repository.UserRepository;
import com.codecool.spaceship.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class NewUserService {

    private final PasswordEncoder passwordEncoder;
    private final LevelService levelService;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final LocationDataGenerator locationDataGenerator;
    private final Random random;

    @Autowired
    public NewUserService(PasswordEncoder passwordEncoder, LevelService levelService, UserRepository userRepository,
                          LocationRepository locationRepository, LocationDataGenerator locationDataGenerator, Random random) {
        this.passwordEncoder = passwordEncoder;
        this.levelService = levelService;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.locationDataGenerator = locationDataGenerator;
        this.random = random;
    }

    public UserEntity createUser(RegisterRequest request) {
        UserEntity newUser = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        SpaceStation spaceStation = SpaceStationManager.createNewSpaceStation("%s's Station".formatted(request.getUsername()));
        MinerShip minerShip = MinerShipManager.createNewMinerShip(levelService, "Miner Ship #1", Color.SAPPHIRE);

        newUser.setSpaceStation(spaceStation);
        spaceStation.setUser(newUser);
        spaceStation.setHangar(Set.of(minerShip));
        minerShip.setStation(spaceStation);
        minerShip.setUser(newUser);
        UserEntity user = userRepository.save(newUser);
        createStarterLocations(user);
        return user;
    }

    private void createStarterLocations(UserEntity user) {
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        Location metalPlanet = Location.builder()
                .name(locationDataGenerator.determineName())
                .discovered(now)
                .distanceFromStation(1)
                .resourceType(ResourceType.METAL)
                .resourceReserve(random.nextInt(200, 500))
                .user(user)
                .build();
        Location crystalPlanet = Location.builder()
                .name(locationDataGenerator.determineName())
                .discovered(now)
                .distanceFromStation(random.nextInt(2,5))
                .resourceType(ResourceType.CRYSTAL)
                .resourceReserve(random.nextInt(100, 400))
                .user(user)
                .build();
        Location siliconePlanet = Location.builder()
                .name(locationDataGenerator.determineName())
                .discovered(now)
                .distanceFromStation(random.nextInt(2,5))
                .resourceType(ResourceType.SILICONE)
                .resourceReserve(random.nextInt(100, 400))
                .user(user)
                .build();
        locationRepository.saveAll(List.of(metalPlanet, crystalPlanet, siliconePlanet));
    }
}
