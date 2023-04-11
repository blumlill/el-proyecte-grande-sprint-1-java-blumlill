package com.codecool.spaceship.auth;

import com.codecool.spaceship.config.JwtService;
import com.codecool.spaceship.model.UserEntity;
import com.codecool.spaceship.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NewUserService newUserService;

    public Cookie register(RegisterRequest request) {
        checkUsernameAndEmailAvailability(request.getUsername(), request.getEmail());
        UserEntity user = newUserService.createUser(request);
        String jwtToken = jwtService.generateToken(user);
        return createCookie(jwtToken);
    }

    public Cookie authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword())
        );
        UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return createCookie(jwtToken);
    }

    public Cookie clearCookie() {
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setDomain(System.getenv("domain"));
        jwtCookie.setPath("/");
        return jwtCookie;
    }

    private void checkUsernameAndEmailAvailability(String username, String email) {
        List<UserEntity> matches = userRepository.findAllByUsernameIsIgnoreCaseOrEmailIsIgnoreCase(username, email);
        if (matches.size() > 1) {
            throw new IllegalArgumentException("Email and username are both in use.");
        } else if (matches.size() == 1) {
            UserEntity match = matches.get(0);
            if (match.getEmail().equalsIgnoreCase(email)
                    && match.getUsername().equalsIgnoreCase(username)) {
                throw new IllegalArgumentException("Email and username are both in use.");
            } else if (match.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("Email is already in use.");
            } else {
                throw new IllegalArgumentException("Username is already in use.");
            }
        }
    }

    private Cookie createCookie(String token) {
        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(1));
        jwtCookie.setDomain(System.getenv("domain"));
        jwtCookie.setPath("/");
        return jwtCookie;
    }
}
