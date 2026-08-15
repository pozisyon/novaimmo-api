package com.novaimmo.demo.project;

import com.novaimmo.demo.exception.BusinessException;
import com.novaimmo.demo.exception.ResourceNotFoundException;
import com.novaimmo.demo.project.dto.CreateProjectRequest;
import com.novaimmo.demo.project.dto.ProjectResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProjectService {

    private static final Set<String> ALLOWED_STATUS =
            Set.of(
                    "ETUDE",
                    "PLANIFIE",
                    "EN_COURS",
                    "TERMINE",
                    "SUSPENDU"
            );

    private final ProjectRepository repository;

    public ProjectService(
            ProjectRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional
    public ProjectResponse create(
            CreateProjectRequest request
    ) {

        Project project = new Project();

        project.setReference(
                generateReference()
        );

        project.setNom(
                request.nom()
        );

        project.setDescription(
                request.description()
        );

        project.setLocalisation(
                request.localisation()
        );

        project.setLatitude(
                request.latitude()
        );

        project.setLongitude(
                request.longitude()
        );

        project.setBudget(
                request.budget()
        );

        project.setDevise(
                request.devise() == null
                        || request.devise().isBlank()
                        ? "USD"
                        : request.devise().toUpperCase()
        );

        project.setImageUrl(
                request.imageUrl()
        );

        project.setStatut(
                "ETUDE"
        );

        return toResponse(
                repository.save(project)
        );
    }

    public List<ProjectResponse> findAll() {

        return repository
                .findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProjectResponse findById(
            Long id
    ) {

        return toResponse(
                findEntity(id)
        );
    }

    public List<ProjectResponse> findActive() {

        return repository
                .findAll()
                .stream()
                .filter(project ->
                        !"TERMINE".equals(project.getStatut())
                                && !"SUSPENDU".equals(project.getStatut())
                )
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProjectResponse changeStatus(
            Long id,
            String newStatus
    ) {

        Project project =
                findEntity(id);

        String status =
                newStatus.toUpperCase();

        if (!ALLOWED_STATUS.contains(status)) {

            throw new BusinessException(
                    "Statut de projet invalide"
            );
        }

        project.setStatut(status);

        return toResponse(
                repository.save(project)
        );
    }

    @Transactional
    public void delete(
            Long id
    ) {

        Project project =
                findEntity(id);

        repository.delete(project);
    }

    private Project findEntity(
            Long id
    ) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Projet introuvable : " + id
                        )
                );
    }

    private String generateReference() {

        return "PRJ-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();
    }

    private ProjectResponse toResponse(
            Project project
    ) {

        return new ProjectResponse(
                project.getId(),
                project.getReference(),
                project.getNom(),
                project.getDescription(),
                project.getLocalisation(),
                project.getLatitude(),
                project.getLongitude(),
                project.getBudget(),
                project.getDevise(),
                project.getStatut(),
                project.getImageUrl(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}