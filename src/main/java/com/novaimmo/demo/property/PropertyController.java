package com.novaimmo.demo.property;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/properties")
    @CrossOrigin(origins = "*")
    public class PropertyController {

        private final PropertyService service;


        public PropertyController(
                PropertyService service
        ) {

            this.service = service;
        }


        @GetMapping
        public List<Property> findAll() {

            return service.findAll();
        }


        @GetMapping("/{id}")
        public Property findById(
                @PathVariable Long id
        ) {

            return service.findById(id);
        }


        @GetMapping("/featured")
        public List<Property> featured() {

            return service.findFeatured();
        }


        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public Property create(
                @RequestBody Property property
        ) {

            return service.create(property);
        }


        @PutMapping("/{id}")
        public Property update(
                @PathVariable Long id,
                @RequestBody Property property
        ) {

            return service.update(
                    id,
                    property
            );
        }


        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void delete(
                @PathVariable Long id
        ) {

            service.delete(id);
        }

}
