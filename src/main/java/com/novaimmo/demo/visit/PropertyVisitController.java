package com.novaimmo.demo.visit;

import com.novaimmo.demo.visit.dto.CreatePropertyVisitRequest;
import com.novaimmo.demo.visit.dto.PropertyVisitResponse;
import com.novaimmo.demo.visit.dto.RescheduleVisitRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class PropertyVisitController {

    private final PropertyVisitService service;


    public PropertyVisitController(
            PropertyVisitService service
    ) {
        this.service = service;
    }


    /*
     * =========================================================
     * CREER UNE VISITE POUR UNE PROPRIETE
     * =========================================================
     */

    @PostMapping(
            "/properties/{propertyId}/visits"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public PropertyVisitResponse create(
            @PathVariable Long propertyId,
            @RequestBody CreatePropertyVisitRequest request
    ) {

        return service.create(
                propertyId,
                request
        );
    }


    /*
     * =========================================================
     * MES VISITES
     * =========================================================
     */

    @GetMapping(
            "/visits/me"
    )
    public List<PropertyVisitResponse> findMyVisits() {

        return service.findMyVisits();
    }


    /*
     * =========================================================
     * TOUTES LES VISITES
     * =========================================================
     */

    @GetMapping(
            "/visits"
    )
    public List<PropertyVisitResponse> findAll() {

        return service.findAll();
    }


    /*
     * =========================================================
     * VISITE PAR ID
     * =========================================================
     */

    @GetMapping(
            "/visits/{id}"
    )
    public PropertyVisitResponse findById(
            @PathVariable Long id
    ) {

        return service.findById(id);
    }


    /*
     * =========================================================
     * VISITES D'UNE PROPRIETE
     * =========================================================
     */

    @GetMapping(
            "/properties/{propertyId}/visits"
    )
    public List<PropertyVisitResponse> findByProperty(
            @PathVariable Long propertyId
    ) {

        return service.findByProperty(
                propertyId
        );
    }


    /*
     * =========================================================
     * VISITES EN ATTENTE
     * =========================================================
     */

    @GetMapping(
            "/visits/pending"
    )
    public List<PropertyVisitResponse> findPending() {

        return service.findPending();
    }


    /*
     * =========================================================
     * CONFIRMER
     * =========================================================
     */

    @PatchMapping(
            "/visits/{id}/confirm"
    )
    public PropertyVisitResponse confirm(
            @PathVariable Long id
    ) {

        return service.confirm(id);
    }


    /*
     * =========================================================
     * REPORTER
     * =========================================================
     */

    @PatchMapping(
            "/visits/{id}/reschedule"
    )
    public PropertyVisitResponse reschedule(
            @PathVariable Long id,
            @RequestBody RescheduleVisitRequest request
    ) {

        return service.reschedule(
                id,
                request
        );
    }


    /*
     * =========================================================
     * ANNULER
     * =========================================================
     */

    @PatchMapping(
            "/visits/{id}/cancel"
    )
    public PropertyVisitResponse cancel(
            @PathVariable Long id
    ) {

        return service.cancel(id);
    }


    /*
     * =========================================================
     * TERMINER
     * =========================================================
     */

    @PatchMapping(
            "/visits/{id}/complete"
    )
    public PropertyVisitResponse complete(
            @PathVariable Long id
    ) {

        return service.complete(id);
    }


    /*
     * =========================================================
     * ASSIGNER UN AGENT
     * =========================================================
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