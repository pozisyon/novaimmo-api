package com.novaimmo.demo.property;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

    public interface PropertyImageRepository
            extends JpaRepository<PropertyImage, Long> {

        List<PropertyImage>
        findByPropertyIdOrderByOrdreAffichageAsc(Long propertyId);

        Optional<PropertyImage>
        findByPropertyIdAndPrincipaleTrue(Long propertyId);

}
