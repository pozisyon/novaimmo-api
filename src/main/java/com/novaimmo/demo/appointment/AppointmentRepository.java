package com.novaimmo.demo.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment>
    findByStatutOrderByDateDebutAsc(
            String statut
    );


    boolean existsByAgentIdAndDateDebut(
            Long agentId,
            LocalDateTime dateDebut
    );


    List<Appointment>
    findByClientIdOrderByDateDebutDesc(
            Long clientId
    );
    long countByAgentId(
            Long agentId
    );
    List<Appointment>
    findByAgentIdOrderByDateDebutAsc(Long agentId);
    Optional<Appointment> findByIdAndAgentId(
            Long id,
            Long agentId
    );

    long countByStatut(String statut);
}