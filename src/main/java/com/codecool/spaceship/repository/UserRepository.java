package com.codecool.spaceship.repository;

import com.codecool.spaceship.model.Role;
import com.codecool.spaceship.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByRole(Role role);
    List<UserEntity> findAllByUsernameIsIgnoreCaseOrEmailIsIgnoreCase(String username, String email);
}
