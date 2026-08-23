package com.novaimmo.demo.property.dto;

public record PropertyImageResponse(

        Long id,

        Long propertyId,

        String imageUrl,

        String titre,

        Boolean principale,

        Integer ordreAffichage

) {
}