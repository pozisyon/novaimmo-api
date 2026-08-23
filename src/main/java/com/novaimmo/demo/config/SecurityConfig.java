package com.novaimmo.demo.config;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                 * ================================
                 * CORS
                 * ================================
                 */
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )


                /*
                 * ================================
                 * CSRF
                 * ================================
                 */
                .csrf(csrf ->
                        csrf.disable()
                )


                /*
                 * ================================
                 * SESSION
                 * ================================
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                /*
                 * ================================
                 * AUTORISATIONS
                 * ================================
                 */
                .authorizeHttpRequests(auth -> auth

                        // ==========================================
                        // AUTH
                        // ==========================================

                        .requestMatchers(
                                "/api/auth/**",
                                "/api/properties/**",
                                "/uploads/**"
                        )
                        .permitAll()


                        // ==========================================
                        // VISITE PUBLIQUE D'UNE PROPRIETE
                        // IMPORTANT : AVANT /api/properties/**
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/*/visits"
                        )
                        .permitAll()


                        // ==========================================
                        // PROPRIETES - CONSULTATION PUBLIQUE
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/properties/**"
                        )
                        .permitAll()


                        // ==========================================
                        // PROPRIETES - CREATION
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        // ==========================================
                        // PROPRIETES - MODIFICATION
                        // ==========================================

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


                        // ==========================================
                        // PROPRIETES - SUPPRESSION
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/properties/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        // ==========================================
                        // CONTACT
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/contacts"
                        )
                        .permitAll()


                        .requestMatchers(
                                "/api/contacts/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        // ==========================================
                        // VISITES - CLIENT
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/visits/me"
                        )
                        .authenticated()


                        // ==========================================
                        // VISITES - ADMIN / AGENT
                        // ==========================================

                        .requestMatchers(
                                "/api/visits/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        // ==========================================
                        // RENDEZ-VOUS
                        // ==========================================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/appointments"
                        )
                        .permitAll()


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/appointments/me"
                        )
                        .authenticated()


                        .requestMatchers(
                                "/api/appointments/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        // ==========================================
                        // PROJETS
                        // ==========================================

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


                        // ==========================================
                        // TRANSACTIONS
                        // ==========================================

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


                        // ==========================================
                        // PAIEMENTS
                        // ==========================================

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


                        // ==========================================
                        // DOCUMENTS
                        // ==========================================

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


                        // ==========================================
                        // PARTENAIRES
                        // ==========================================

                        .requestMatchers(
                                "/api/partners/**"
                        )
                        .hasAnyRole(
                                "ADMIN",
                                "AGENT"
                        )


                        .requestMatchers(
                                "/api/agent/**"
                        )
                        .hasRole("AGENT")

                        // ==========================================
                        // ADMIN
                        // ==========================================

                        .requestMatchers(
                                "/api/admin/**"
                        )
                        .hasRole("ADMIN")


                        // ==========================================
                        // PUBLIC
                        // ==========================================

                        .requestMatchers(
                                "/",
                                "/error"
                        )
                        .permitAll()


                        .anyRequest()
                        .authenticated()
                )


                .authenticationProvider(
                        authenticationProvider()
                )


                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        /*
         * Frontend Angular local
         */
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:4200"
                )
        );

        /*
         * Méthodes HTTP utilisées par l'API
         */
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        /*
         * Headers envoyés notamment par Angular
         * et plus tard par notre JWT interceptor.
         */
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept"
                )
        );

        /*
         * Headers que le navigateur peut lire.
         */
        configuration.setExposedHeaders(
                List.of(
                        "Authorization"
                )
        );

        /*
         * Pour le moment nous n'utilisons pas
         * de cookie de session.
         *
         * L'authentification repose sur JWT.
         */
        configuration.setAllowCredentials(false);

        /*
         * Cache du preflight CORS.
         */
        configuration.setMaxAge(3600L);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}