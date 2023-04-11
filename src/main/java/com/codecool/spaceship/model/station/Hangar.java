package com.codecool.spaceship.model.station;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Hangar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
