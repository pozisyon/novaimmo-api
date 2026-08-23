package com.novaimmo.demo.admin.dto;

import java.math.BigDecimal;

public record AdminDashboardResponse(

        long properties,

        long users,

        long clients,

        long agents,

        long visits,

        long appointments,

        long transactions,

        long payments,

        long pendingVisits,

        long pendingAppointments,

        long activeTransactions,

        BigDecimal totalPayments

) {
}