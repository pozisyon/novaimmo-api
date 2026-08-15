package com.novaimmo.demo.auth;



import com.novaimmo.demo.auth.dto.*;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService service;

    public AuthController(
            AuthService service
    ) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(

            @Valid
            @RequestBody
            RegisterRequest request
    ) {

        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(

            @Valid
            @RequestBody
            LoginRequest request
    ) {

        return service.login(request);
    }
}