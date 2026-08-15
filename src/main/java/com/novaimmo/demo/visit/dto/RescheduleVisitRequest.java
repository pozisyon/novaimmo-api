package com.novaimmo.demo.visit.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RescheduleVisitRequest(

        @NotNull
        @Future
        LocalDateTime nouvelleDate,

        String commentaire

) {
}