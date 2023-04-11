package com.codecool.spaceship.model.ship;

import com.codecool.spaceship.model.resource.ResourceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MinerShip extends SpaceShip {
    private int drillLevel;
    private int storageLevel;
    @ElementCollection
    @CollectionTable(
            name = "minership_storage_mapping",
            joinColumns = @JoinColumn(name = "ship_id", referencedColumnName = "id")
    )
    @MapKeyJoinColumn(name = "resource")
    @Column(name = "amount")
    private Map<ResourceType, Integer> storedResources;

}
