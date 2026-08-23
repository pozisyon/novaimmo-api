package com.novaimmo.demo.admin;

import com.novaimmo.demo.visit.PropertyVisitService;
import com.novaimmo.demo.visit.dto.PropertyVisitResponse;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/visits")
public class AdminVisitController {

    private final PropertyVisitService visitService;

    public AdminVisitController(
            PropertyVisitService visitService
    ) {
        this.visitService =
                visitService;
    }


    @PatchMapping(
            "/{visitId}/assign/{agentId}"
    )
    public PropertyVisitResponse assignAgent(
            @PathVariable Long visitId,
            @PathVariable Long agentId
    ) {

        return visitService
                .assignAgent(
                        visitId,
                        agentId
                );
    }
}