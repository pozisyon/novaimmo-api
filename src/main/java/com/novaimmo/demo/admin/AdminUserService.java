package com.novaimmo.demo.admin;

import com.novaimmo.demo.admin.dto.AdminUserResponse;
import com.novaimmo.demo.admin.dto.CreateAgentRequest;

import com.novaimmo.demo.user.Role;
import com.novaimmo.demo.user.RoleRepository;
import com.novaimmo.demo.user.User;
import com.novaimmo.demo.user.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    public AdminUserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {

        this.userRepository =
                userRepository;

        this.roleRepository =
                roleRepository;

        this.passwordEncoder =
                passwordEncoder;
    }


    // =========================================================
    // LISTE DES UTILISATEURS
    // =========================================================

    public List<AdminUserResponse> findAll() {

        return userRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================================================
    // UTILISATEUR PAR ID
    // =========================================================

    public AdminUserResponse findById(
            Long id
    ) {

        return toResponse(
                findEntity(id)
        );
    }


    // =========================================================
    // CREATION D'UN AGENT
    // =========================================================

    @Transactional
    public AdminUserResponse createAgent(
            CreateAgentRequest request
    ) {

        if (
                request.email() == null
                        ||
                        request.email().isBlank()
        ) {

            throw new RuntimeException(
                    "L'email est obligatoire"
            );
        }


        String email =
                request.email()
                        .trim()
                        .toLowerCase();


        if (
                userRepository
                        .existsByEmail(email)
        ) {

            throw new RuntimeException(
                    "Un utilisateur existe déjà avec cet email"
            );
        }


        if (
                request.password() == null
                        ||
                        request.password().length() < 6
        ) {

            throw new RuntimeException(
                    "Le mot de passe doit contenir au moins 6 caractères"
            );
        }


        Role agentRole =
                roleRepository
                        .findByCode(
                                "AGENT"
                        )
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Le rôle AGENT est introuvable"
                                        )
                        );


        User user =
                new User();


        user.setRole(
                agentRole
        );

        user.setNom(
                request.nom()
        );

        user.setPrenom(
                request.prenom()
        );

        user.setEmail(
                email
        );

        user.setTelephone(
                request.telephone()
        );

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.password()
                )
        );

        user.setActif(
                true
        );


        return toResponse(
                userRepository.save(
                        user
                )
        );
    }


    // =========================================================
    // ACTIVER UN COMPTE
    // =========================================================

    @Transactional
    public AdminUserResponse activate(
            Long id
    ) {

        User user =
                findEntity(id);


        user.setActif(
                true
        );


        return toResponse(
                userRepository.save(
                        user
                )
        );
    }


    // =========================================================
    // DESACTIVER UN COMPTE
    // =========================================================

    @Transactional
    public AdminUserResponse deactivate(
            Long id
    ) {

        User user =
                findEntity(id);


        user.setActif(
                false
        );


        return toResponse(
                userRepository.save(
                        user
                )
        );
    }


    // =========================================================
    // RECHERCHE INTERNE
    // =========================================================

    private User findEntity(
            Long id
    ) {

        return userRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new RuntimeException(
                                        "Utilisateur introuvable"
                                )
                );
    }


    // =========================================================
    // DTO
    // =========================================================

    private AdminUserResponse toResponse(
            User user
    ) {

        return new AdminUserResponse(

                user.getId(),

                user
                        .getRole()
                        .getId(),

                user
                        .getRole()
                        .getCode(),

                user
                        .getRole()
                        .getNom(),

                user.getNom(),

                user.getPrenom(),

                user.getEmail(),

                user.getTelephone(),

                user.getActif(),

                user.getCreatedAt(),

                user.getUpdatedAt()
        );
    }
    public List<AdminUserResponse> findAgents() {

        return userRepository
                .findByRole_CodeOrderByNomAsc(
                        "AGENT"
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }
}