package com.novaimmo.demo.admin;

import com.novaimmo.demo.appointment.AppointmentService;
import com.novaimmo.demo.appointment.dto.AppointmentResponse;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/appointments")
public class AdminAppointmentController {

    private final AppointmentService appointmentService;


    public AdminAppointmentController(
            AppointmentService appointmentService
    ) {
        this.appointmentService =
                appointmentService;
    }


    @PatchMapping(
            "/{appointmentId}/assign/{agentId}"
    )
    public AppointmentResponse assignAgent(
            @PathVariable Long appointmentId,
            @PathVariable Long agentId
    ) {

        return appointmentService
                .assignAgent(
                        appointmentId,
                        agentId
                );
    }
}