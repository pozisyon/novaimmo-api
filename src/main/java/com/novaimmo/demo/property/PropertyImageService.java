package com.novaimmo.demo.property;

import com.novaimmo.demo.property.dto.CreatePropertyImageRequest;
import com.novaimmo.demo.property.dto.PropertyImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
    @Service
    public class PropertyImageService {

        private final PropertyRepository propertyRepository;
        private final PropertyImageRepository imageRepository;
        private final PropertyImageStorageService storageService;

        public PropertyImageService(
                PropertyRepository propertyRepository,
                PropertyImageRepository imageRepository,
                PropertyImageStorageService storageService
        ) {

            this.propertyRepository =
                    propertyRepository;

            this.imageRepository =
                    imageRepository;

            this.storageService =
                    storageService;
        }


        public List<PropertyImageResponse> findByProperty(
                Long propertyId
        ) {

            verifyProperty(propertyId);

            return imageRepository
                    .findByPropertyIdOrderByOrdreAffichageAsc(propertyId)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }


        @Transactional
        public PropertyImageResponse create(
                Long propertyId,
                CreatePropertyImageRequest request
        ) {

            Property property = propertyRepository
                    .findById(propertyId)
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Propriété introuvable : " + propertyId
                            )
                    );


            boolean principale =
                    Boolean.TRUE.equals(request.principale());


            /*
             * Si la nouvelle image devient principale,
             * on retire l'ancienne image principale.
             */
            if (principale) {

                imageRepository
                        .findByPropertyIdAndPrincipaleTrue(propertyId)
                        .ifPresent(image -> {

                            image.setPrincipale(false);

                            imageRepository.save(image);
                        });
            }


            PropertyImage image = new PropertyImage();

            image.setProperty(property);

            image.setImageUrl(
                    request.imageUrl()
            );

            image.setTitre(
                    request.titre()
            );

            image.setPrincipale(
                    principale
            );

            image.setOrdreAffichage(
                    request.ordreAffichage() == null
                            ? 0
                            : request.ordreAffichage()
            );


            PropertyImage saved =
                    imageRepository.save(image);

            return toResponse(saved);
        }


        @Transactional
        public PropertyImageResponse setPrincipale(
                Long propertyId,
                Long imageId
        ) {

            verifyProperty(propertyId);


            PropertyImage image =
                    imageRepository
                            .findById(imageId)
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Image introuvable"
                                    )
                            );


            if (!image.getProperty()
                    .getId()
                    .equals(propertyId)) {

                throw new RuntimeException(
                        "Cette image n'appartient pas à cette propriété"
                );
            }


            imageRepository
                    .findByPropertyIdAndPrincipaleTrue(propertyId)
                    .ifPresent(current -> {

                        current.setPrincipale(false);

                        imageRepository.save(current);
                    });


            image.setPrincipale(true);

            return toResponse(
                    imageRepository.save(image)
            );
        }


        @Transactional
        public void delete(
                Long propertyId,
                Long imageId
        ) {

            verifyProperty(propertyId);


            PropertyImage image =
                    imageRepository
                            .findById(imageId)
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Image introuvable"
                                    )
                            );


            if (!image.getProperty()
                    .getId()
                    .equals(propertyId)) {

                throw new RuntimeException(
                        "Cette image n'appartient pas à cette propriété"
                );
            }


            imageRepository.delete(image);
        }


        private void verifyProperty(
                Long propertyId
        ) {

            if (!propertyRepository.existsById(propertyId)) {

                throw new RuntimeException(
                        "Propriété introuvable : " + propertyId
                );
            }
        }


        private PropertyImageResponse toResponse(
                PropertyImage image
        ) {

            return new PropertyImageResponse(

                    image.getId(),

                    image.getProperty().getId(),

                    image.getImageUrl(),

                    image.getTitre(),

                    image.getPrincipale(),

                    image.getOrdreAffichage()
            );
        }

        @Transactional
        public PropertyImageResponse upload(
                Long propertyId,
                MultipartFile file,
                String titre,
                Boolean principale,
                Integer ordreAffichage
        ) {

            Property property =
                    propertyRepository
                            .findById(propertyId)
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Propriété introuvable : "
                                                    + propertyId
                                    )
                            );


            boolean main =
                    Boolean.TRUE.equals(
                            principale
                    );


            if (main) {

                imageRepository
                        .findByPropertyIdAndPrincipaleTrue(
                                propertyId
                        )
                        .ifPresent(
                                current -> {

                                    current.setPrincipale(
                                            false
                                    );

                                    imageRepository.save(
                                            current
                                    );
                                }
                        );
            }


            String imageUrl =
                    storageService.store(
                            propertyId,
                            file
                    );


            PropertyImage image =
                    new PropertyImage();


            image.setProperty(
                    property
            );


            image.setImageUrl(
                    imageUrl
            );


            image.setTitre(
                    titre
            );


            image.setPrincipale(
                    main
            );


            image.setOrdreAffichage(
                    ordreAffichage == null
                            ? 0
                            : ordreAffichage
            );


            PropertyImage saved =
                    imageRepository.save(
                            image
                    );


            return toResponse(
                    saved
            );
        }
}
