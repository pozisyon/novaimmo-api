package com.novaimmo.demo.auth;



import com.novaimmo.demo.user.User;
import com.novaimmo.demo.user.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {

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
                                "Utilisateur authentifié introuvable"
                        )
                );
    }

    public Long getCurrentUserId() {

        return getCurrentUser()
                .getId();
    }

    public String getCurrentUserRole() {

        return getCurrentUser()
                .getRole()
                .getCode();
    }

    public boolean isAuthenticated() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(
                authentication.getPrincipal()
        );
    }

    public User getCurrentUserOrNull() {

        if (!isAuthenticated()) {
            return null;
        }

        return getCurrentUser();
    }
}
