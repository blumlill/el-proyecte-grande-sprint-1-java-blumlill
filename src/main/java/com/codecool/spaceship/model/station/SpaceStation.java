package com.codecool.spaceship.model.station;

import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.model.resource.ResourceType;
import com.codecool.spaceship.model.ship.SpaceShip;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "spacestation")
public class SpaceStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int storageLevel;
    private int hangarLevel;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name ="station_id")
    private Set<SpaceShip> hangar;
    @ElementCollection
    @CollectionTable(
            name = "station_storage_mapping",
            joinColumns = @JoinColumn(name = "station_id", referencedColumnName = "id")
    )
    @MapKeyJoinColumn(name = "resource")
    @Column(name = "amount")
    private Map<ResourceType, Integer> storedResources;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
