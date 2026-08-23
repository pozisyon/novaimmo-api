package com.novaimmo.demo.property;

import com.novaimmo.demo.property.dto.PropertyResponse;

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
    public List<PropertyResponse> findAll() {

        return service.findAll();
    }


    @GetMapping("/{id}")
    public PropertyResponse findById(
            @PathVariable Long id
    ) {

        return service.findById(id);
    }


    @GetMapping("/featured")
    public List<PropertyResponse> featured() {

        return service.findFeatured();
    }
}