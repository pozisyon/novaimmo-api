package com.novaimmo.demo.agent.dto;

public record AgentDashboardResponse(

        long assignedVisits,

        long assignedAppointments,

        long assignedTransactions,

        long activeTransactions

) {
}