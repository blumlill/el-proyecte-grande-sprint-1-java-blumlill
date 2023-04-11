package com.codecool.spaceship.model.location;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.mission.Mission;
import com.codecool.spaceship.model.resource.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime discovered;
    private int distanceFromStation;
    @Enumerated(value = EnumType.STRING)
    private ResourceType resourceType;
    private int resourceReserve;
    @OneToOne
    private Mission currentMission;
    @ManyToOne
    private UserEntity user;
}
