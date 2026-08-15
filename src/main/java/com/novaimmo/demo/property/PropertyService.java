package com.novaimmo.demo.property;


import org.springframework.stereotype.Service;

import java.util.List;

    @Service
    public class PropertyService {

        private final PropertyRepository repository;

        public PropertyService(PropertyRepository repository) {
            this.repository = repository;
        }


        public List<Property> findAll() {

            return repository.findAll();
        }


        public Property findById(Long id) {

            return repository.findById(id)
                    .orElseThrow(
                            () -> new RuntimeException(
                                    "Propriété introuvable"
                            )
                    );
        }


        public List<Property> findFeatured() {

            return repository.findByFeaturedTrue();
        }


        public Property create(Property property) {

            return repository.save(property);
        }


        public Property update(
                Long id,
                Property data
        ) {

            Property property = findById(id);

            property.setTitre(data.getTitre());
            property.setDescription(data.getDescription());
            property.setPrix(data.getPrix());
            property.setQuartier(data.getQuartier());
            property.setStatut(data.getStatut());
            property.setFeatured(data.getFeatured());

            return repository.save(property);
        }


        public void delete(Long id) {

            Property property = findById(id);

            repository.delete(property);
        }

}
