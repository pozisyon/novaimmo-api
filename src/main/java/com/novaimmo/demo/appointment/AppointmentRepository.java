package com.novaimmo.demo.appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    List<Appointment>
    findByStatutOrderByDateDebutAsc(
            String statut
    );

    List<Appointment>
    findByAgentIdOrderByDateDebutAsc(
            Long agentId
    );

    List<Appointment>
    findByClientIdOrderByDateDebutDesc(
            Long clientId
    );

    boolean existsByAgentIdAndDateDebut(
            Long agentId,
            LocalDateTime dateDebut
    );
}