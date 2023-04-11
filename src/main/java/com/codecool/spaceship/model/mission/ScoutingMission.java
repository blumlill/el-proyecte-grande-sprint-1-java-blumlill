package com.codecool.spaceship.model.mission;

import com.codecool.spaceship.model.location.Location;
import com.codecool.spaceship.model.resource.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScoutingMission extends Mission {

    @Enumerated(EnumType.STRING)
    private ResourceType targetResource;
    private int distance;
    private boolean prioritizingDistance;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Location discoveredLocation;

}
