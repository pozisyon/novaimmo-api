package com.novaimmo.demo.config;

import com.novaimmo.demo.auth.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(
            UserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {

        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    /*
     * =========================================================
     * PASSWORD ENCODER
     * =========================================================
     *
     * Tous les mots de passe sont stockés sous forme BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    /*
     * =========================================================
     * AUTHENTICATION PROVIDER
     * =========================================================
     *
     * Utilise notre UserDetailsService pour retrouver
     * l'utilisateur dans la base de données.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }


    /*
     * =========================================================
     * AUTHENTICATION MANAGER
     * =========================================================
     *
     * Utilisé notamment par AuthService lors du login.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {

        return authenticationConfiguration
                .getAuthenticationManager();
    }


    /*
     * =========================================================
     * SECURITY FILTER CHAIN
     * =========================================================
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                /*
                 * =================================================
                 * CSRF
                 * =================================================
                 *
                 * L'application utilise une API REST avec JWT.
                 * Nous n'utilisons donc pas une session classique.
                 */
                .csrf(csrf ->
                        csrf.disable()
                )


                /*
                 * =================================================
                 * SESSION
                 * =================================================
                 *
                 * JWT = application stateless.
                 */
                .sessionManagement(session ->

                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                /*
                 * =================================================
                 * AUTORISATIONS
                 * =================================================
                 */
                .authorizeHttpRequests(auth -> auth


                        /*
                         * =========================================
                         * AUTHENTIFICATION
                         * =========================================
                         *
                         * Login et inscription accessibles
                         * sans JWT.
                         */
                        .requestMatchers(
                                "/api/auth/**"
                        )
                        .permitAll()


                        /*
                         * =========================================
                         * PROPRIETES - CONSULTATION PUBLIQUE
                         * =========================================
                         *
                         * Tout le monde peut consulter
                         * les annonces immobilières.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/properties/**"
                        )
                        .permitAll()


                        /*
                         * =========================================
                         * PROPRIETES - CREATION
                         * =========================================
                         *
                         * Seuls ADMIN et AGENT peuvent
                         * publier une propriété.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        /*
                         * =========================================
                         * PROPRIETES - MODIFICATION
                         * =========================================
                         */
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        /*
                         * =========================================
                         * PROPRIETES - SUPPRESSION
                         * =========================================
                         */
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        /*
                         * =========================================
                         * CONTACT
                         * =========================================
                         *
                         * Un visiteur peut envoyer un message
                         * depuis le site vitrine.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/contacts"
                        )
                        .permitAll()


                        /*
                         * Seuls les agents et administrateurs
                         * peuvent consulter et traiter
                         * les messages.
                         */
                        .requestMatchers(
                                "/api/contacts/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        /*
                         * =========================================
                         * VISITES IMMOBILIERES
                         * =========================================
                         *
                         * Une personne peut demander une visite
                         * même sans compte.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/*/visits"
                        )
                        .permitAll()


                        /*
                         * Client connecté :
                         * consulter uniquement ses visites.
                         *
                         * IMPORTANT :
                         * cette règle doit être placée AVANT
                         * /api/visits/**
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/visits/me"
                        )
                        .authenticated()


                        /*
                         * Gestion des visites.
                         *
                         * Liste globale,
                         * confirmation,
                         * report,
                         * affectation agent,
                         * visite effectuée, etc.
                         */
                        .requestMatchers(
                                "/api/visits/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        /*
                         * =========================================
                         * RENDEZ-VOUS
                         * =========================================
                         *
                         * Une personne peut demander
                         * un rendez-vous sans compte.
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/appointments"
                        )
                        .permitAll()


                        /*
                         * Client connecté :
                         * consulter ses rendez-vous.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointments/me"
                        )
                        .authenticated()


                        /*
                         * Gestion administrative
                         * des rendez-vous.
                         */
                        .requestMatchers(
                                "/api/appointments/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        /*
                         * =========================================
                         * TRANSACTIONS
                         * =========================================
                         *
                         * Pour le moment, seuls ADMIN et AGENT
                         * ont accès aux opérations générales
                         * sur les transactions.
                         *
                         * Nous ajouterons /transactions/me
                         * lorsque le module sera implémenté.
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/transactions/me"
                        )
                        .authenticated()
                        .requestMatchers(
                                "/api/transactions/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        /*
                         * =========================================
                         * PAIEMENTS
                         * =========================================
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/payments/me"
                        )
                        .authenticated()
                        .requestMatchers(
                                "/api/payments/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )
// =============================
// DOCUMENTS DE TRANSACTION
// =============================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/transaction-documents/me"
                                )
                                .authenticated()

                                .requestMatchers(
                                        "/api/transaction-documents/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "AGENT"
                                )

                                // =============================
// PROJETS
// =============================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/projects/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/api/projects/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "AGENT"
                                )

                                // =============================
// PARTENAIRES
// =============================

                                .requestMatchers(
                                        "/api/partners/**"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "AGENT"
                                )

                        /*
                         * =========================================
                         * ADMINISTRATION
                         * =========================================
                         *
                         * Zone exclusivement réservée
                         * aux administrateurs.
                         */
                        .requestMatchers(
                                "/api/admin/**"
                        )
                        .hasRole(
                                "ADMIN"
                        )


                        /*
                         * =========================================
                         * RACINE / ERREURS
                         * =========================================
                         */
                        .requestMatchers(
                                "/",
                                "/error"
                        )
                        .permitAll()


                        /*
                         * =========================================
                         * AUTRES ENDPOINTS
                         * =========================================
                         *
                         * Par défaut, toute route non déclarée
                         * nécessite au minimum un JWT valide.
                         */
                        .anyRequest()
                        .authenticated()
                )


                /*
                 * =================================================
                 * AUTHENTICATION PROVIDER
                 * =================================================
                 */
                .authenticationProvider(
                        authenticationProvider()
                )


                /*
                 * =================================================
                 * JWT FILTER
                 * =================================================
                 *
                 * Le filtre JWT est exécuté avant le filtre
                 * d'authentification standard de Spring Security.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}