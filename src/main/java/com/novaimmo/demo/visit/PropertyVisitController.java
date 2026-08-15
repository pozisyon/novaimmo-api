package com.novaimmo.demo.visit;


import com.novaimmo.demo.visit.dto.CreatePropertyVisitRequest;
import com.novaimmo.demo.visit.dto.PropertyVisitResponse;
import com.novaimmo.demo.visit.dto.RescheduleVisitRequest;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class PropertyVisitController {

    private final PropertyVisitService service;


    public PropertyVisitController(
            PropertyVisitService service
    ) {
        this.service = service;
    }


    /*
     * Client :
     * demander une visite
     */
    @PostMapping("/properties/{propertyId}/visits")
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyVisitResponse create(

            @PathVariable Long propertyId,

            @Valid
            @RequestBody
            CreatePropertyVisitRequest request
    ) {

        return service.create(
                propertyId,
                request
        );
    }


    /*
     * Administration
     */
    @GetMapping("/visits")
    public List<PropertyVisitResponse> findAll() {

        return service.findAll();
    }


    @GetMapping("/visits/{id}")
    public PropertyVisitResponse findById(

            @PathVariable Long id
    ) {

        return service.findById(id);
    }


    @GetMapping("/visits/pending")
    public List<PropertyVisitResponse> pending() {

        return service.findPending();
    }


    /*
     * Toutes les visites d'une propriété
     */
    @GetMapping("/properties/{propertyId}/visits")
    public List<PropertyVisitResponse> findByProperty(

            @PathVariable Long propertyId
    ) {

        return service.findByProperty(
                propertyId
        );
    }


    /*
     * Confirmer
     */
    @PatchMapping("/visits/{id}/confirm")
    public PropertyVisitResponse confirm(

            @PathVariable Long id
    ) {

        return service.confirm(id);
    }


    /*
     * Reporter
     */
    @PatchMapping("/visits/{id}/reschedule")
    public PropertyVisitResponse reschedule(

            @PathVariable Long id,

            @Valid
            @RequestBody
            RescheduleVisitRequest request
    ) {

        return service.reschedule(
                id,
                request
        );
    }


    /*
     * Annuler
     */
    @PatchMapping("/visits/{id}/cancel")
    public PropertyVisitResponse cancel(

            @PathVariable Long id
    ) {

        return service.cancel(id);
    }


    /*
     * Marquer comme effectuée
     */
    @PatchMapping("/visits/{id}/complete")
    public PropertyVisitResponse complete(

            @PathVariable Long id
    ) {

        return service.complete(id);
    }


    /*
     * Affectation d'un agent
     */
    @PatchMapping(
            "/visits/{visitId}/agent/{agentId}"
    )
    public PropertyVisitResponse assignAgent(

            @PathVariable Long visitId,

            @PathVariable Long agentId
    ) {

        return service.assignAgent(
                visitId,
                agentId
        );
    }
}