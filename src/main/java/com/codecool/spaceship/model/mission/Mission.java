package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.ship.SpaceShip;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Transactional
public abstract class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime currentObjectiveTime;
    private LocalDateTime approxEndTime;
    @Enumerated(value = EnumType.STRING)
    private MissionStatus currentStatus;
    private long travelDurationInSecs;
    private long activityDurationInSecs;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ship_id")
    private SpaceShip ship;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "mission_id")
    private List<Event> events;
}
