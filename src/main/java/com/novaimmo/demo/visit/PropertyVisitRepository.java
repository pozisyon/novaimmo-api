package com.novaimmo.demo.visit;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PropertyVisitRepository
        extends JpaRepository<PropertyVisit, Long> {

    List<PropertyVisit>
    findByPropertyIdOrderByDateVisiteAsc(Long propertyId);

    List<PropertyVisit>
    findByStatutOrderByDateVisiteAsc(String statut);

    List<PropertyVisit>
    findByAgentIdOrderByDateVisiteAsc(Long agentId);

    List<PropertyVisit>
    findByClientIdOrderByDateVisiteDesc(Long clientId);

    boolean existsByPropertyIdAndDateVisite(
            Long propertyId,
            LocalDateTime dateVisite
    );
    long countByAgentId(
            Long agentId
    );

    Optional<PropertyVisit> findByIdAndAgentId(
            Long id,
            Long agentId
    );
    long countByStatut(String statut);


}