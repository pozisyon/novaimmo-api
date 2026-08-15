package com.novaimmo.demo.appointment;

import com.novaimmo.demo.appointment.dto.AppointmentResponse;
import com.novaimmo.demo.appointment.dto.CreateAppointmentRequest;
import com.novaimmo.demo.appointment.dto.RescheduleAppointmentRequest;

import com.novaimmo.demo.auth.CurrentUserService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository repository;
    private final CurrentUserService currentUserService;


    public AppointmentService(
            AppointmentRepository repository,
            CurrentUserService currentUserService
    ) {
        this.repository = repository;
        this.currentUserService = currentUserService;
    }


    /*
     * =========================================================
     * CREATION D'UN RENDEZ-VOUS
     * =========================================================
     */
    @Transactional
    public AppointmentResponse create(
            CreateAppointmentRequest request
    ) {

        /*
         * Vérification de cohérence des dates.
         */
        if (request.dateFin() != null
                && request.dateFin()
                .isBefore(request.dateDebut())) {

            throw new RuntimeException(
                    "La date de fin ne peut pas précéder la date de début"
            );
        }


        Appointment appointment =
                new Appointment();


        /*
         * =====================================================
         * ASSOCIATION AUTOMATIQUE AU CLIENT CONNECTE
         * =====================================================
         *
         * Si la personne possède un JWT valide,
         * son identifiant est automatiquement récupéré.
         *
         * Sinon, le rendez-vous reste associé
         * à un visiteur anonyme.
         */
        if (currentUserService.isAuthenticated()) {

            appointment.setClientId(
                    currentUserService.getCurrentUserId()
            );

        } else {

            appointment.setClientId(null);
        }


        appointment.setNomContact(
                request.nomContact()
        );

        appointment.setEmail(
                request.email()
        );

        appointment.setTelephone(
                request.telephone()
        );

        appointment.setSujet(
                request.sujet()
        );

        appointment.setDateDebut(
                request.dateDebut()
        );

        appointment.setDateFin(
                request.dateFin()
        );

        appointment.setLieu(
                request.lieu()
        );

        appointment.setNotes(
                request.notes()
        );

        appointment.setStatut(
                "DEMANDE"
        );


        return toResponse(
                repository.save(appointment)
        );
    }


    /*
     * =========================================================
     * LISTE COMPLETE
     * =========================================================
     *
     * Destinée principalement aux AGENT / ADMIN.
     */
    public List<AppointmentResponse> findAll() {

        return repository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    /*
     * =========================================================
     * RENDEZ-VOUS EN ATTENTE
     * =========================================================
     */
    public List<AppointmentResponse> findPending() {

        return repository
                .findByStatutOrderByDateDebutAsc(
                        "DEMANDE"
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    /*
     * =========================================================
     * RENDEZ-VOUS DU CLIENT CONNECTE
     * =========================================================
     */
    public List<AppointmentResponse> findMyAppointments() {

        Long clientId =
                currentUserService
                        .getCurrentUserId();


        return repository
                .findByClientIdOrderByDateDebutDesc(
                        clientId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    /*
     * =========================================================
     * RECHERCHE PAR ID
     * =========================================================
     */
    public AppointmentResponse findById(
            Long id
    ) {

        return toResponse(
                findEntity(id)
        );
    }


    /*
     * =========================================================
     * CONFIRMER
     * =========================================================
     */
    @Transactional
    public AppointmentResponse confirm(
            Long id
    ) {

        Appointment appointment =
                findEntity(id);


        if (!"DEMANDE".equals(
                appointment.getStatut()
        )
                && !"REPORTE".equals(
                appointment.getStatut()
        )) {

            throw new RuntimeException(
                    "Ce rendez-vous ne peut pas être confirmé"
            );
        }


        appointment.setStatut(
                "CONFIRME"
        );


        return toResponse(
                repository.save(appointment)
        );
    }


    /*
     * =========================================================
     * ANNULER
     * =========================================================
     */
    @Transactional
    public AppointmentResponse cancel(
            Long id
    ) {

        Appointment appointment =
                findEntity(id);


        if ("TERMINE".equals(
                appointment.getStatut()
        )) {

            throw new RuntimeException(
                    "Un rendez-vous terminé ne peut pas être annulé"
            );
        }


        /*
         * Évite de réannuler plusieurs fois.
         */
        if ("ANNULE".equals(
                appointment.getStatut()
        )) {

            throw new RuntimeException(
                    "Ce rendez-vous est déjà annulé"
            );
        }


        appointment.setStatut(
                "ANNULE"
        );


        return toResponse(
                repository.save(appointment)
        );
    }


    /*
     * =========================================================
     * TERMINER
     * =========================================================
     */
    @Transactional
    public AppointmentResponse complete(
            Long id
    ) {

        Appointment appointment =
                findEntity(id);


        if (!"CONFIRME".equals(
                appointment.getStatut()
        )) {

            throw new RuntimeException(
                    "Seul un rendez-vous confirmé peut être terminé"
            );
        }


        appointment.setStatut(
                "TERMINE"
        );


        return toResponse(
                repository.save(appointment)
        );
    }


    /*
     * =========================================================
     * REPORTER
     * =========================================================
     */
    @Transactional
    public AppointmentResponse reschedule(
            Long id,
            RescheduleAppointmentRequest request
    ) {

        Appointment appointment =
                findEntity(id);


        if ("ANNULE".equals(
                appointment.getStatut()
        )
                || "TERMINE".equals(
                appointment.getStatut()
        )) {

            throw new RuntimeException(
                    "Ce rendez-vous ne peut plus être reporté"
            );
        }


        if (request.nouvelleDateFin() != null
                && request.nouvelleDateFin()
                .isBefore(
                        request.nouvelleDateDebut()
                )) {

            throw new RuntimeException(
                    "La nouvelle date de fin est invalide"
            );
        }


        /*
         * Si un agent est déjà affecté,
         * on vérifie qu'il n'a pas déjà
         * un rendez-vous sur ce créneau.
         */
        if (appointment.getAgentId() != null) {

            boolean occupied =
                    repository
                            .existsByAgentIdAndDateDebut(
                                    appointment.getAgentId(),
                                    request.nouvelleDateDebut()
                            );


            /*
             * Si la date ne change pas,
             * la requête peut retrouver le rendez-vous courant.
             */
            if (occupied
                    && !appointment.getDateDebut()
                    .equals(request.nouvelleDateDebut())) {

                throw new RuntimeException(
                        "L'agent a déjà un rendez-vous à cette heure"
                );
            }
        }


        appointment.setDateDebut(
                request.nouvelleDateDebut()
        );

        appointment.setDateFin(
                request.nouvelleDateFin()
        );


        if (request.notes() != null
                && !request.notes().isBlank()) {

            appointment.setNotes(
                    request.notes()
            );
        }


        appointment.setStatut(
                "REPORTE"
        );


        return toResponse(
                repository.save(appointment)
        );
    }


    /*
     * =========================================================
     * AFFECTATION D'UN AGENT
     * =========================================================
     */
    @Transactional
    public AppointmentResponse assignAgent(
            Long appointmentId,
            Long agentId
    ) {

        Appointment appointment =
                findEntity(appointmentId);


        if ("ANNULE".equals(
                appointment.getStatut()
        )
                || "TERMINE".equals(
                appointment.getStatut()
        )) {

            throw new RuntimeException(
                    "Impossible d'affecter un agent à ce rendez-vous"
            );
        }


        boolean occupied =
                repository
                        .existsByAgentIdAndDateDebut(
                                agentId,
                                appointment.getDateDebut()
                        );


        if (occupied
                && !agentId.equals(
                appointment.getAgentId()
        )) {

            throw new RuntimeException(
                    "Cet agent a déjà un rendez-vous à cette heure"
            );
        }


        appointment.setAgentId(
                agentId
        );


        return toResponse(
                repository.save(appointment)
        );
    }


    /*
     * =========================================================
     * ENTITE INTERNE
     * =========================================================
     */
    private Appointment findEntity(
            Long id
    ) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Rendez-vous introuvable : " + id
                        )
                );
    }


    /*
     * =========================================================
     * MAPPING ENTITE -> DTO
     * =========================================================
     */
    private AppointmentResponse toResponse(
            Appointment appointment
    ) {

        return new AppointmentResponse(

                appointment.getId(),

                appointment.getClientId(),

                appointment.getAgentId(),

                appointment.getNomContact(),

                appointment.getEmail(),

                appointment.getTelephone(),

                appointment.getSujet(),

                appointment.getDateDebut(),

                appointment.getDateFin(),

                appointment.getLieu(),

                appointment.getStatut(),

                appointment.getNotes(),

                appointment.getCreatedAt()
        );
    }
}