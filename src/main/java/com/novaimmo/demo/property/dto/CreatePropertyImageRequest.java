package com.novaimmo.demo.property.dto;


    public record CreatePropertyImageRequest(

            String imageUrl,

            String titre,

            Boolean principale,

            Integer ordreAffichage

    ) {

}
