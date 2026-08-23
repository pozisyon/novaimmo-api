package com.novaimmo.demo.property;

import com.novaimmo.demo.property.dto.CreatePropertyRequest;
import com.novaimmo.demo.property.dto.PropertyImageResponse;
import com.novaimmo.demo.property.dto.PropertyResponse;
import com.novaimmo.demo.property.dto.UpdatePropertyRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class PropertyService {

    private final PropertyRepository repository;

    private final PropertyImageRepository imageRepository;

    private final PropertyTypeRepository typeRepository;


    public PropertyService(
            PropertyRepository repository,
            PropertyImageRepository imageRepository,
            PropertyTypeRepository typeRepository
    ) {

        this.repository =
                repository;

        this.imageRepository =
                imageRepository;

        this.typeRepository =
                typeRepository;
    }


    // =========================================================
    // LISTE DES PROPRIETES
    // =========================================================

    public List<PropertyResponse> findAll() {

        return repository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================================================
    // PROPRIETE PAR ID
    // =========================================================

    public PropertyResponse findById(
            Long id
    ) {

        Property property =
                repository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Propriété introuvable : " + id
                                )
                        );


        return toResponse(
                property
        );
    }


    // =========================================================
    // PROPRIETES EN VEDETTE
    // =========================================================

    public List<PropertyResponse> findFeatured() {

        return repository
                .findByFeaturedTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }


    // =========================================================
    // CREATION D'UNE PROPRIETE
    // =========================================================

    @Transactional
    public PropertyResponse create(
            CreatePropertyRequest request
    ) {

        /*
         * Vérification référence.
         */
        if (
                request.reference() == null
                        ||
                        request.reference().isBlank()
        ) {

            throw new RuntimeException(
                    "La référence est obligatoire"
            );
        }


        String reference =
                request.reference()
                        .trim()
                        .toUpperCase();


        if (
                repository
                        .existsByReference(reference)
        ) {

            throw new RuntimeException(
                    "Une propriété existe déjà avec cette référence"
            );
        }


        /*
         * Vérification titre.
         */
        if (
                request.titre() == null
                        ||
                        request.titre().isBlank()
        ) {

            throw new RuntimeException(
                    "Le titre est obligatoire"
            );
        }


        /*
         * Type de propriété.
         */
        if (
                request.typeId() == null
        ) {

            throw new RuntimeException(
                    "Le type de propriété est obligatoire"
            );
        }


        PropertyType type =
                typeRepository
                        .findById(
                                request.typeId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Type de propriété introuvable"
                                )
                        );


        Property property =
                new Property();


        property.setReference(
                reference
        );


        property.setType(
                type
        );


        property.setTitre(
                request.titre().trim()
        );


        property.setDescription(
                request.description()
        );


        property.setTransactionType(
                normalize(
                        request.transactionType()
                )
        );


        property.setPrix(
                request.prix()
        );


        property.setDevise(
                request.devise() == null
                        ||
                        request.devise().isBlank()

                        ? "USD"

                        : request.devise()
                        .trim()
                        .toUpperCase()
        );


        property.setAdresse(
                request.adresse()
        );


        property.setQuartier(
                request.quartier()
        );


        property.setVille(
                request.ville()
        );


        property.setDepartement(
                request.departement()
        );


        property.setPays(
                request.pays()
        );


        property.setLatitude(
                request.latitude()
        );


        property.setLongitude(
                request.longitude()
        );


        property.setChambres(
                request.chambres()
        );


        property.setSallesBain(
                request.sallesBain()
        );


        property.setSuperficie(
                request.superficie()
        );


        property.setStatut(
                request.statut() == null
                        ||
                        request.statut().isBlank()

                        ? "DISPONIBLE"

                        : request.statut()
                        .trim()
                        .toUpperCase()
        );


        property.setFeatured(
                Boolean.TRUE.equals(
                        request.featured()
                )
        );


        LocalDateTime now =
                LocalDateTime.now();


        property.setCreatedAt(
                now
        );


        property.setUpdatedAt(
                now
        );


        Property saved =
                repository.save(
                        property
                );


        return toResponse(
                saved
        );
    }


    // =========================================================
    // MODIFICATION D'UNE PROPRIETE
    // =========================================================

    @Transactional
    public PropertyResponse update(
            Long id,
            UpdatePropertyRequest request
    ) {

        Property property =
                repository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Propriété introuvable : " + id
                                )
                        );


        /*
         * Type.
         */
        if (
                request.typeId() != null
        ) {

            PropertyType type =
                    typeRepository
                            .findById(
                                    request.typeId()
                            )
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Type de propriété introuvable"
                                    )
                            );


            property.setType(
                    type
            );
        }


        /*
         * On ne remplace les champs
         * que lorsqu'ils sont présents.
         */


        if (
                request.titre() != null
                        &&
                        !request.titre().isBlank()
        ) {

            property.setTitre(
                    request.titre().trim()
            );
        }


        if (
                request.description() != null
        ) {

            property.setDescription(
                    request.description()
            );
        }


        if (
                request.transactionType() != null
        ) {

            property.setTransactionType(
                    normalize(
                            request.transactionType()
                    )
            );
        }


        if (
                request.prix() != null
        ) {

            property.setPrix(
                    request.prix()
            );
        }


        if (
                request.devise() != null
                        &&
                        !request.devise().isBlank()
        ) {

            property.setDevise(
                    request.devise()
                            .trim()
                            .toUpperCase()
            );
        }


        if (
                request.adresse() != null
        ) {

            property.setAdresse(
                    request.adresse()
            );
        }


        if (
                request.quartier() != null
        ) {

            property.setQuartier(
                    request.quartier()
            );
        }


        if (
                request.ville() != null
        ) {

            property.setVille(
                    request.ville()
            );
        }


        if (
                request.departement() != null
        ) {

            property.setDepartement(
                    request.departement()
            );
        }


        if (
                request.pays() != null
        ) {

            property.setPays(
                    request.pays()
            );
        }


        if (
                request.latitude() != null
        ) {

            property.setLatitude(
                    request.latitude()
            );
        }


        if (
                request.longitude() != null
        ) {

            property.setLongitude(
                    request.longitude()
            );
        }


        if (
                request.chambres() != null
        ) {

            property.setChambres(
                    request.chambres()
            );
        }


        if (
                request.sallesBain() != null
        ) {

            property.setSallesBain(
                    request.sallesBain()
            );
        }


        if (
                request.superficie() != null
        ) {

            property.setSuperficie(
                    request.superficie()
            );
        }


        if (
                request.statut() != null
                        &&
                        !request.statut().isBlank()
        ) {

            property.setStatut(
                    request.statut()
                            .trim()
                            .toUpperCase()
            );
        }


        if (
                request.featured() != null
        ) {

            property.setFeatured(
                    request.featured()
            );
        }


        property.setUpdatedAt(
                LocalDateTime.now()
        );


        Property saved =
                repository.save(
                        property
                );


        return toResponse(
                saved
        );
    }


    // =========================================================
    // CHANGEMENT DE STATUT
    // =========================================================

    @Transactional
    public PropertyResponse updateStatus(
            Long id,
            String statut
    ) {

        Property property =
                repository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Propriété introuvable : " + id
                                )
                        );


        if (
                statut == null
                        ||
                        statut.isBlank()
        ) {

            throw new RuntimeException(
                    "Le statut est obligatoire"
            );
        }


        String newStatus =
                statut
                        .trim()
                        .toUpperCase();


        /*
         * On limite les valeurs acceptées.
         */
        if (
                !List.of(
                        "DISPONIBLE",
                        "RESERVEE",
                        "VENDUE",
                        "LOUEE",
                        "INACTIVE"
                ).contains(newStatus)
        ) {

            throw new RuntimeException(
                    "Statut de propriété invalide : "
                            + newStatus
            );
        }


        property.setStatut(
                newStatus
        );


        property.setUpdatedAt(
                LocalDateTime.now()
        );


        return toResponse(
                repository.save(
                        property
                )
        );
    }


    // =========================================================
    // FEATURED
    // =========================================================

    @Transactional
    public PropertyResponse toggleFeatured(
            Long id
    ) {

        Property property =
                repository
                        .findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Propriété introuvable : " + id
                                )
                        );


        boolean current =
                Boolean.TRUE.equals(
                        property.getFeatured()
                );


        property.setFeatured(
                !current
        );


        property.setUpdatedAt(
                LocalDateTime.now()
        );


        return toResponse(
                repository.save(
                        property
                )
        );
    }


    // =========================================================
    // MAPPING ENTITY -> RESPONSE
    // =========================================================

    private PropertyResponse toResponse(
            Property property
    ) {

        List<PropertyImageResponse> images =
                imageRepository
                        .findByPropertyIdOrderByOrdreAffichageAsc(
                                property.getId()
                        )
                        .stream()
                        .map(
                                image ->
                                        new PropertyImageResponse(

                                                image.getId(),

                                                property.getId(),

                                                image.getImageUrl(),

                                                image.getTitre(),

                                                image.getPrincipale(),

                                                image.getOrdreAffichage()
                                        )
                        )
                        .toList();


        String mainImageUrl =
                imageRepository
                        .findByPropertyIdAndPrincipaleTrue(
                                property.getId()
                        )
                        .map(
                                PropertyImage::getImageUrl
                        )
                        .orElseGet(
                                () ->

                                        images.isEmpty()

                                                ? null

                                                : images
                                                .get(0)
                                                .imageUrl()
                        );


        return new PropertyResponse(

                property.getId(),

                property.getReference(),

                property
                        .getType()
                        .getId(),

                property
                        .getType()
                        .getCode(),

                property
                        .getType()
                        .getNom(),

                property.getTitre(),

                property.getDescription(),

                property.getTransactionType(),

                property.getPrix(),

                property.getDevise(),

                property.getAdresse(),

                property.getQuartier(),

                property.getVille(),

                property.getDepartement(),

                property.getPays(),

                property.getLatitude(),

                property.getLongitude(),

                property.getChambres(),

                property.getSallesBain(),

                property.getSuperficie(),

                property.getStatut(),

                property.getFeatured(),

                mainImageUrl,

                images,

                property.getCreatedAt(),

                property.getUpdatedAt()
        );
    }


    // =========================================================
    // NORMALISATION
    // =========================================================

    private String normalize(
            String value
    ) {

        if (
                value == null
                        ||
                        value.isBlank()
        ) {

            return null;
        }


        return value
                .trim()
                .toUpperCase();
    }
}