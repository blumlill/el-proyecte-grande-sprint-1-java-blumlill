package com.codecool.spaceship.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public Boolean register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        Cookie jwtCookie = authenticationService.register(request);
        response.addCookie(jwtCookie);
        return true;
    }

    @PostMapping("/authenticate")
    public Boolean authenticate(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        Cookie jwtCookie = authenticationService.authenticate(request);
        response.addCookie(jwtCookie);
        return true;
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie clearCookie = authenticationService.clearCookie();
        response.addCookie(clearCookie);
    }
}
