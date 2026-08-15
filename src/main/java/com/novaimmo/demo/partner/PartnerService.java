package com.novaimmo.demo.partner;

import com.novaimmo.demo.exception.BusinessException;
import com.novaimmo.demo.exception.ResourceNotFoundException;

import com.novaimmo.demo.partner.dto.CreatePartnerRequest;
import com.novaimmo.demo.partner.dto.PartnerResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class PartnerService {

    private static final Set<String> ALLOWED_TYPES =
            Set.of(
                    "INVESTISSEUR",
                    "PROMOTEUR",
                    "PROPRIETAIRE",
                    "ENTREPRISE",
                    "INSTITUTION"
            );

    private final PartnerRepository repository;

    public PartnerService(
            PartnerRepository repository
    ) {

        this.repository = repository;
    }

    @Transactional
    public PartnerResponse create(
            CreatePartnerRequest request
    ) {

        String type =
                request.typePartenaire()
                        .toUpperCase();

        if (!ALLOWED_TYPES.contains(type)) {

            throw new BusinessException(
                    "Type de partenaire invalide"
            );
        }

        Partner partner =
                new Partner();

        partner.setNom(
                request.nom()
        );

        partner.setEntreprise(
                request.entreprise()
        );

        partner.setEmail(
                request.email()
        );

        partner.setTelephone(
                request.telephone()
        );

        partner.setTypePartenaire(
                type
        );

        partner.setActif(true);

        return toResponse(
                repository.save(partner)
        );
    }

    public List<PartnerResponse> findAll() {

        return repository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PartnerResponse> findActive() {

        return repository
                .findByActifTrueOrderByNomAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PartnerResponse deactivate(
            Long id
    ) {

        Partner partner =
                findEntity(id);

        partner.setActif(false);

        return toResponse(
                repository.save(partner)
        );
    }

    @Transactional
    public PartnerResponse activate(
            Long id
    ) {

        Partner partner =
                findEntity(id);

        partner.setActif(true);

        return toResponse(
                repository.save(partner)
        );
    }

    private Partner findEntity(
            Long id
    ) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Partenaire introuvable : " + id
                        )
                );
    }

    private PartnerResponse toResponse(
            Partner partner
    ) {

        return new PartnerResponse(
                partner.getId(),
                partner.getNom(),
                partner.getEntreprise(),
                partner.getEmail(),
                partner.getTelephone(),
                partner.getTypePartenaire(),
                partner.getActif(),
                partner.getCreatedAt()
        );
    }
}