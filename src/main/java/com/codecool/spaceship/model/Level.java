package com.codecool.spaceship.model;

import com.codecool.spaceship.model.resource.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Level {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private UpgradeableType type;
    private Integer level;
    private Integer effect;
    private boolean max;
    @ElementCollection
    @CollectionTable(
            name = "level_cost_mapping",
            joinColumns = @JoinColumn(name = "level_id", referencedColumnName = "id")
    )
    @MapKeyJoinColumn(name = "resource")
    @Column(name = "amount")
    private Map<ResourceType, Integer> cost;
}
