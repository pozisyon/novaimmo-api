package com.novaimmo.demo.visit;



import com.novaimmo.demo.property.Property;
import com.novaimmo.demo.property.PropertyRepository;
import com.novaimmo.demo.visit.dto.CreatePropertyVisitRequest;
import com.novaimmo.demo.visit.dto.PropertyVisitResponse;
import com.novaimmo.demo.visit.dto.RescheduleVisitRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.novaimmo.demo.auth.CurrentUserService;
import java.util.List;

@Service
public class PropertyVisitService {

    private final PropertyVisitRepository visitRepository;
    private final PropertyRepository propertyRepository;
    private final CurrentUserService currentUserService;

    public PropertyVisitService(
            PropertyVisitRepository visitRepository,
            PropertyRepository propertyRepository,
            CurrentUserService currentUserService
    ) {

        this.visitRepository = visitRepository;
        this.propertyRepository = propertyRepository;
        this.currentUserService = currentUserService;
    }


    @Transactional
    public PropertyVisitResponse create(
            Long propertyId,
            CreatePropertyVisitRequest request
    ) {

        Property property = propertyRepository
                .findById(propertyId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Propriété introuvable"
                        )
                );


        /*
         * On évite par exemple de visiter
         * une propriété déjà vendue ou inactive.
         */
        if (!"DISPONIBLE".equalsIgnoreCase(
                property.getStatut()
        )) {

            throw new RuntimeException(
                    "Cette propriété n'est pas disponible pour une visite"
            );
        }


        boolean alreadyReserved =
                visitRepository
                        .existsByPropertyIdAndDateVisite(
                                propertyId,
                                request.dateVisite()
                        );


        if (alreadyReserved) {

            throw new RuntimeException(
                    "Une visite est déjà prévue à cette heure"
            );
        }


        PropertyVisit visit =
                new PropertyVisit();

        visit.setProperty(property);

        visit.setNomVisiteur(
                request.nomVisiteur()
        );

        visit.setEmail(
                request.email()
        );

        visit.setTelephone(
                request.telephone()
        );

        visit.setDateVisite(
                request.dateVisite()
        );

        visit.setNombrePersonnes(
                request.nombrePersonnes() == null
                        ? 1
                        : request.nombrePersonnes()
        );

        visit.setCommentaire(
                request.commentaire()
        );

        visit.setStatut("DEMANDEE");
        visit.setProperty(property);

        if (currentUserService.isAuthenticated()) {

            visit.setClientId(
                    currentUserService.getCurrentUserId()
            );

        } else {

            visit.setClientId(null);
        }

        return toResponse(
                visitRepository.save(visit)
        );
    }


    public List<PropertyVisitResponse> findAll() {

        return visitRepository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public PropertyVisitResponse findById(
            Long id
    ) {

        return toResponse(
                findEntity(id)
        );
    }


    public List<PropertyVisitResponse>
    findByProperty(Long propertyId) {

        return visitRepository
                .findByPropertyIdOrderByDateVisiteAsc(
                        propertyId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    public List<PropertyVisitResponse>
    findPending() {

        return visitRepository
                .findByStatutOrderByDateVisiteAsc(
                        "DEMANDEE"
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional
    public PropertyVisitResponse confirm(
            Long id
    ) {

        PropertyVisit visit =
                findEntity(id);

        verifyStatus(
                visit,
                "DEMANDEE",
                "REPORTEE"
        );

        visit.setStatut(
                "CONFIRMEE"
        );

        return toResponse(
                visitRepository.save(visit)
        );
    }


    @Transactional
    public PropertyVisitResponse reschedule(
            Long id,
            RescheduleVisitRequest request
    ) {

        PropertyVisit visit =
                findEntity(id);


        if ("ANNULEE".equals(visit.getStatut())
                || "EFFECTUEE".equals(visit.getStatut())) {

            throw new RuntimeException(
                    "Cette visite ne peut plus être reportée"
            );
        }


        boolean occupied =
                visitRepository
                        .existsByPropertyIdAndDateVisite(
                                visit.getProperty().getId(),
                                request.nouvelleDate()
                        );


        if (occupied) {

            throw new RuntimeException(
                    "Ce créneau est déjà occupé"
            );
        }


        visit.setDateVisite(
                request.nouvelleDate()
        );

        visit.setStatut(
                "REPORTEE"
        );


        if (request.commentaire() != null) {

            visit.setCommentaire(
                    request.commentaire()
            );
        }


        return toResponse(
                visitRepository.save(visit)
        );
    }


    @Transactional
    public PropertyVisitResponse cancel(
            Long id
    ) {

        PropertyVisit visit =
                findEntity(id);


        if ("EFFECTUEE".equals(
                visit.getStatut()
        )) {

            throw new RuntimeException(
                    "Une visite effectuée ne peut pas être annulée"
            );
        }


        visit.setStatut(
                "ANNULEE"
        );


        return toResponse(
                visitRepository.save(visit)
        );
    }


    @Transactional
    public PropertyVisitResponse complete(
            Long id
    ) {

        PropertyVisit visit =
                findEntity(id);


        if (!"CONFIRMEE".equals(
                visit.getStatut()
        )) {

            throw new RuntimeException(
                    "Seule une visite confirmée peut être marquée comme effectuée"
            );
        }


        visit.setStatut(
                "EFFECTUEE"
        );


        return toResponse(
                visitRepository.save(visit)
        );
    }


    @Transactional
    public PropertyVisitResponse assignAgent(
            Long visitId,
            Long agentId
    ) {

        PropertyVisit visit =
                findEntity(visitId);

        visit.setAgentId(agentId);

        return toResponse(
                visitRepository.save(visit)
        );
    }


    private PropertyVisit findEntity(
            Long id
    ) {

        return visitRepository
                .findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Visite introuvable"
                        )
                );
    }


    private void verifyStatus(
            PropertyVisit visit,
            String... authorizedStatuses
    ) {

        for (String status : authorizedStatuses) {

            if (status.equals(
                    visit.getStatut()
            )) {
                return;
            }
        }

        throw new RuntimeException(
                "Opération non autorisée pour le statut "
                        + visit.getStatut()
        );
    }


    private PropertyVisitResponse toResponse(
            PropertyVisit visit
    ) {

        return new PropertyVisitResponse(

                visit.getId(),

                visit.getProperty().getId(),

                visit.getProperty().getReference(),

                visit.getProperty().getTitre(),

                visit.getClientId(),

                visit.getAgentId(),

                visit.getNomVisiteur(),

                visit.getEmail(),

                visit.getTelephone(),

                visit.getDateVisite(),

                visit.getNombrePersonnes(),

                visit.getStatut(),

                visit.getCommentaire(),

                visit.getCreatedAt()
        );
    }
    public List<PropertyVisitResponse> findMyVisits() {

        Long clientId =
                currentUserService
                        .getCurrentUserId();

        return visitRepository
                .findByClientIdOrderByDateVisiteDesc(
                        clientId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }
}