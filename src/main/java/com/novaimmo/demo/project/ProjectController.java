package com.novaimmo.demo.project;

import com.novaimmo.demo.project.dto.CreateProjectRequest;
import com.novaimmo.demo.project.dto.ProjectResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService service;

    public ProjectController(
            ProjectService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<ProjectResponse> findAll() {

        return service.findAll();
    }

    @GetMapping("/active")
    public List<ProjectResponse> active() {

        return service.findActive();
    }

    @GetMapping("/{id}")
    public ProjectResponse findById(
            @PathVariable Long id
    ) {

        return service.findById(id);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(

            @Valid
            @RequestBody
            CreateProjectRequest request
    ) {

        return service.create(request);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','AGENT')"
    )
    @PatchMapping("/{id}/status/{status}")
    public ProjectResponse changeStatus(

            @PathVariable Long id,

            @PathVariable String status
    ) {

        return service.changeStatus(
                id,
                status
        );
    }

    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {

        service.delete(id);
    }
}