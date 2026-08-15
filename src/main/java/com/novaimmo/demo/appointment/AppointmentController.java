package com.novaimmo.demo.appointment;



import com.novaimmo.demo.appointment.dto.AppointmentResponse;
import com.novaimmo.demo.appointment.dto.CreateAppointmentRequest;
import com.novaimmo.demo.appointment.dto.RescheduleAppointmentRequest;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(
            AppointmentService service
    ) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse create(

            @Valid
            @RequestBody
            CreateAppointmentRequest request
    ) {

        return service.create(request);
    }

    @GetMapping
    public List<AppointmentResponse> findAll() {

        return service.findAll();
    }

    @GetMapping("/pending")
    public List<AppointmentResponse> pending() {

        return service.findPending();
    }

    @GetMapping("/{id}")
    public AppointmentResponse findById(
            @PathVariable Long id
    ) {

        return service.findById(id);
    }

    @PatchMapping("/{id}/confirm")
    public AppointmentResponse confirm(
            @PathVariable Long id
    ) {

        return service.confirm(id);
    }

    @PatchMapping("/{id}/cancel")
    public AppointmentResponse cancel(
            @PathVariable Long id
    ) {

        return service.cancel(id);
    }

    @PatchMapping("/{id}/complete")
    public AppointmentResponse complete(
            @PathVariable Long id
    ) {

        return service.complete(id);
    }

    @PatchMapping("/{id}/reschedule")
    public AppointmentResponse reschedule(

            @PathVariable Long id,

            @Valid
            @RequestBody
            RescheduleAppointmentRequest request
    ) {

        return service.reschedule(
                id,
                request
        );
    }

    @PatchMapping("/{appointmentId}/agent/{agentId}")
    public AppointmentResponse assignAgent(

            @PathVariable Long appointmentId,

            @PathVariable Long agentId
    ) {

        return service.assignAgent(
                appointmentId,
                agentId
        );
    }

    @GetMapping("/me")
    public List<AppointmentResponse> myAppointments() {

        return service.findMyAppointments();
    }
}