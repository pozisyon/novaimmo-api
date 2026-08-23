package com.novaimmo.demo.property;


import org.springframework.web.multipart.MultipartFile;
import com.novaimmo.demo.property.dto.CreatePropertyImageRequest;
import com.novaimmo.demo.property.dto.PropertyImageResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/properties/{propertyId}/images")
    @CrossOrigin(origins = "*")
    public class PropertyImageController {

        private final PropertyImageService service;

        public PropertyImageController(
                PropertyImageService service
        ) {
            this.service = service;
        }


        /*
         * GET
         * /api/properties/1/images
         */
        @GetMapping
        public List<PropertyImageResponse> findAll(
                @PathVariable Long propertyId
        ) {

            return service.findByProperty(propertyId);
        }


        /*
         * POST
         * /api/properties/1/images
         */
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public PropertyImageResponse create(

                @PathVariable Long propertyId,

                @RequestBody
                CreatePropertyImageRequest request
        ) {

            return service.create(
                    propertyId,
                    request
            );
        }


        /*
         * PATCH
         * /api/properties/1/images/2/principale
         */
        @PatchMapping("/{imageId}/principale")
        public PropertyImageResponse setPrincipale(

                @PathVariable Long propertyId,

                @PathVariable Long imageId
        ) {

            return service.setPrincipale(
                    propertyId,
                    imageId
            );
        }


        /*
         * DELETE
         * /api/properties/1/images/2
         */
        @DeleteMapping("/{imageId}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(

                @PathVariable Long propertyId,

                @PathVariable Long imageId
        ) {

            service.delete(
                    propertyId,
                    imageId
            );
        }

        @PostMapping(
                value = "/upload",
                consumes = "multipart/form-data"
        )
        @ResponseStatus(HttpStatus.CREATED)
        public PropertyImageResponse upload(

                @PathVariable Long propertyId,

                @RequestParam("file")
                MultipartFile file,

                @RequestParam(
                        value = "titre",
                        required = false
                )
                String titre,

                @RequestParam(
                        value = "principale",
                        defaultValue = "false"
                )
                Boolean principale,

                @RequestParam(
                        value = "ordreAffichage",
                        defaultValue = "0"
                )
                Integer ordreAffichage
        ) {

            return service.upload(
                    propertyId,
                    file,
                    titre,
                    principale,
                    ordreAffichage
            );
        }

}
