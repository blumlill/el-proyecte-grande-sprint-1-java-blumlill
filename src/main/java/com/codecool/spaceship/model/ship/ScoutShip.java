package com.codecool.spaceship.model.ship;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ScoutShip extends SpaceShip {

    private int scannerLevel;
}
