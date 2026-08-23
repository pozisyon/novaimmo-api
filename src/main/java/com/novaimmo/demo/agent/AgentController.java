package com.novaimmo.demo.agent;

import com.novaimmo.demo.agent.dto.AgentDashboardResponse;

import com.novaimmo.demo.agent.dto.UpdateAppointmentStatusRequest;
import com.novaimmo.demo.agent.dto.UpdateTransactionStatusRequest;
import com.novaimmo.demo.agent.dto.UpdateVisitStatusRequest;
import com.novaimmo.demo.appointment.dto.AppointmentResponse;
import com.novaimmo.demo.transaction.dto.TransactionResponse;
import com.novaimmo.demo.visit.dto.PropertyVisitResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final AgentService agentService;


    public AgentController(
            AgentService agentService
    ) {
        this.agentService =
                agentService;
    }


    @GetMapping("/dashboard")
    public AgentDashboardResponse dashboard() {

        return agentService
                .getDashboard();
    }
    @GetMapping("/visits")
    public List<PropertyVisitResponse> findMyVisits() {

        return agentService
                .findMyVisits();
    }
    @GetMapping("/appointments")
    public List<AppointmentResponse> findMyAppointments() {

        return agentService
                .findMyAppointments();
    }
    @GetMapping("/transactions")
    public List<TransactionResponse> findMyTransactions() {

        return agentService
                .findMyTransactions();
    }
    @PatchMapping("/visits/{visitId}/status")
    public PropertyVisitResponse updateVisitStatus(
            @PathVariable Long visitId,
            @RequestBody UpdateVisitStatusRequest request
    ) {
        return agentService.updateVisitStatus(
                visitId,
                request.statut()
        );
    }
    @PatchMapping(
            "/appointments/{appointmentId}/status"
    )
    public AppointmentResponse updateAppointmentStatus(
            @PathVariable Long appointmentId,
            @RequestBody UpdateAppointmentStatusRequest request
    ) {

        return agentService
                .updateAppointmentStatus(
                        appointmentId,
                        request.statut()
                );
    }
    @PatchMapping(
            "/transactions/{transactionId}/status"
    )
    public TransactionResponse updateTransactionStatus(
            @PathVariable Long transactionId,
            @RequestBody UpdateTransactionStatusRequest request
    ) {

        return agentService
                .updateTransactionStatus(
                        transactionId,
                        request.statut()
                );
    }
}