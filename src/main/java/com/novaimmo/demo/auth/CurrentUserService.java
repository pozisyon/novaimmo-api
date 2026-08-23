package com.novaimmo.demo.auth;

import com.novaimmo.demo.user.User;
import com.novaimmo.demo.user.UserRepository;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    /*
     * =========================================================
     * AUTHENTIFICATION
     * =========================================================
     */

    public boolean isAuthenticated() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication
                instanceof AnonymousAuthenticationToken);
    }


    /*
     * =========================================================
     * UTILISATEUR CONNECTE
     *
     * Cette méthode garde la signature utilisée
     * par les anciens services NovaImmo.
     * =========================================================
     */

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if (
                authentication == null
                        ||
                        !authentication.isAuthenticated()
                        ||
                        authentication
                                instanceof AnonymousAuthenticationToken
        ) {

            throw new RuntimeException(
                    "Utilisateur non authentifié"
            );
        }


        String email =
                authentication.getName();


        return userRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Utilisateur connecté introuvable"
                        )
                );
    }


    /*
     * =========================================================
     * ID UTILISATEUR
     *
     * Signature Long conservée pour :
     *
     * PaymentService
     * TransactionService
     * TransactionDocumentService
     * PropertyVisitService
     * =========================================================
     */

    public Long getCurrentUserId() {

        return getCurrentUser()
                .getId();
    }


    /*
     * =========================================================
     * VARIANTES OPTIONALES
     *
     * Utiles pour les endpoints publics :
     * rendez-vous / visites.
     * =========================================================
     */

    public Optional<User> getCurrentUserOptional() {

        if (!isAuthenticated()) {

            return Optional.empty();
        }


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        String email =
                authentication.getName();


        return userRepository
                .findByEmail(email);
    }


    public Optional<Long> getCurrentUserIdOptional() {

        return getCurrentUserOptional()
                .map(User::getId);
    }
}