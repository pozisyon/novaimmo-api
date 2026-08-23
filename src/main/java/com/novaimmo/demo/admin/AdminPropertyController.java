package com.novaimmo.demo.admin;

import com.novaimmo.demo.property.PropertyService;

import com.novaimmo.demo.property.dto.CreatePropertyRequest;
import com.novaimmo.demo.property.dto.PropertyResponse;
import com.novaimmo.demo.property.dto.UpdatePropertyRequest;
import com.novaimmo.demo.property.dto.UpdatePropertyStatusRequest;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/properties")
public class AdminPropertyController {

    private final PropertyService service;


    public AdminPropertyController(
            PropertyService service
    ) {

        this.service =
                service;
    }


    @GetMapping
    public List<PropertyResponse> findAll() {

        return service.findAll();
    }


    @GetMapping("/{id}")
    public PropertyResponse findById(
            @PathVariable Long id
    ) {

        return service.findById(
                id
        );
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyResponse create(
            @RequestBody
            CreatePropertyRequest request
    ) {

        return service.create(
                request
        );
    }


    @PutMapping("/{id}")
    public PropertyResponse update(
            @PathVariable Long id,
            @RequestBody
            UpdatePropertyRequest request
    ) {

        return service.update(
                id,
                request
        );
    }


    @PatchMapping("/{id}/status")
    public PropertyResponse updateStatus(
            @PathVariable Long id,
            @RequestBody
            UpdatePropertyStatusRequest request
    ) {

        return service.updateStatus(
                id,
                request.statut()
        );
    }


    @PatchMapping("/{id}/featured")
    public PropertyResponse toggleFeatured(
            @PathVariable Long id
    ) {

        return service.toggleFeatured(
                id
        );
    }
}