package com.novaimmo.demo.auth;



import com.novaimmo.demo.auth.dto.AuthResponse;
import com.novaimmo.demo.auth.dto.LoginRequest;
import com.novaimmo.demo.auth.dto.RegisterRequest;

import com.novaimmo.demo.user.*;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public AuthResponse register(
            RegisterRequest request
    ) {

        if (userRepository
                .existsByEmail(request.email())) {

            throw new RuntimeException(
                    "Cette adresse email est déjà utilisée"
            );
        }

        Role clientRole =
                roleRepository
                        .findByCode("CLIENT")
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Rôle CLIENT introuvable"
                                )
                        );

        User user = new User();

        user.setNom(request.nom());
        user.setPrenom(request.prenom());
        user.setEmail(request.email());
        user.setTelephone(request.telephone());

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.password()
                )
        );

        user.setRole(clientRole);
        user.setActif(true);

        User saved =
                userRepository.save(user);

        String token =
                jwtService.generateToken(saved);

        return new AuthResponse(
                token,
                saved.getId(),
                saved.getNom(),
                saved.getEmail(),
                saved.getRole().getCode()
        );
    }

    public AuthResponse login(
            LoginRequest request
    ) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user =
                userRepository
                        .findByEmail(request.email())
                        .orElseThrow();

        String token =
                jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getNom(),
                user.getEmail(),
                user.getRole().getCode()
        );
    }
}