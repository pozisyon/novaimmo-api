package com.novaimmo.demo.partner;

import com.novaimmo.demo.partner.dto.CreatePartnerRequest;
import com.novaimmo.demo.partner.dto.PartnerResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
public class PartnerController {

    private final PartnerService service;

    public PartnerController(
            PartnerService service
    ) {

        this.service = service;
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @GetMapping
    public List<PartnerResponse> findAll() {

        return service.findAll();
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @GetMapping("/active")
    public List<PartnerResponse> findActive() {

        return service.findActive();
    }

    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartnerResponse create(

            @Valid
            @RequestBody
            CreatePartnerRequest request
    ) {

        return service.create(request);
    }

    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    @PatchMapping("/{id}/activate")
    public PartnerResponse activate(
            @PathVariable Long id
    ) {

        return service.activate(id);
    }

    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    @PatchMapping("/{id}/deactivate")
    public PartnerResponse deactivate(
            @PathVariable Long id
    ) {

        return service.deactivate(id);
    }
}