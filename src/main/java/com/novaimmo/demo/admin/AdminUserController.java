package com.novaimmo.demo.admin;

import com.novaimmo.demo.admin.dto.AdminUserResponse;
import com.novaimmo.demo.admin.dto.CreateAgentRequest;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService service;


    public AdminUserController(
            AdminUserService service
    ) {

        this.service =
                service;
    }


    // =========================================================
    // LISTE
    // =========================================================

    @GetMapping
    public List<AdminUserResponse> findAll() {

        return service.findAll();
    }


    // =========================================================
    // DETAIL
    // =========================================================

    @GetMapping("/{id}")
    public AdminUserResponse findById(
            @PathVariable Long id
    ) {

        return service.findById(
                id
        );
    }


    // =========================================================
    // CREER UN AGENT
    // =========================================================

    @PostMapping("/agents")
    @ResponseStatus(HttpStatus.CREATED)
    public AdminUserResponse createAgent(
            @RequestBody CreateAgentRequest request
    ) {

        return service.createAgent(
                request
        );
    }


    // =========================================================
    // ACTIVER
    // =========================================================

    @PatchMapping("/{id}/activate")
    public AdminUserResponse activate(
            @PathVariable Long id
    ) {

        return service.activate(
                id
        );
    }


    // =========================================================
    // DESACTIVER
    // =========================================================

    @PatchMapping("/{id}/deactivate")
    public AdminUserResponse deactivate(
            @PathVariable Long id
    ) {

        return service.deactivate(
                id
        );
    }

    @GetMapping("/agents")
    public List<AdminUserResponse> findAgents() {

        return service.findAgents();
    }
}