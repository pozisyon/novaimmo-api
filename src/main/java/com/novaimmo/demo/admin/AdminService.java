package com.novaimmo.demo.admin;

import com.novaimmo.demo.admin.dto.AdminDashboardResponse;
import com.novaimmo.demo.admin.dto.AdminUserResponse;
import com.novaimmo.demo.appointment.AppointmentRepository;
import com.novaimmo.demo.payment.Payment;
import com.novaimmo.demo.payment.PaymentRepository;
import com.novaimmo.demo.payment.dto.PaymentResponse;
import com.novaimmo.demo.property.PropertyRepository;
import com.novaimmo.demo.transaction.TransactionRepository;
import com.novaimmo.demo.user.UserRepository;
import com.novaimmo.demo.visit.PropertyVisitRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PropertyVisitRepository visitRepository;
    private final AppointmentRepository appointmentRepository;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;


    public AdminService(
            PropertyRepository propertyRepository,
            UserRepository userRepository,
            PropertyVisitRepository visitRepository,
            AppointmentRepository appointmentRepository,
            TransactionRepository transactionRepository,
            PaymentRepository paymentRepository
    ) {

        this.propertyRepository =
                propertyRepository;

        this.userRepository =
                userRepository;

        this.visitRepository =
                visitRepository;

        this.appointmentRepository =
                appointmentRepository;

        this.transactionRepository =
                transactionRepository;

        this.paymentRepository =
                paymentRepository;
    }


    public AdminDashboardResponse getDashboard() {

        long properties =
                propertyRepository.count();

        long users =
                userRepository.count();

        long clients =
                userRepository
                        .countByRole_Code(
                                "CLIENT"
                        );

        long agents =
                userRepository
                        .countByRole_Code(
                                "AGENT"
                        );

        long visits =
                visitRepository.count();

        long appointments =
                appointmentRepository.count();

        long transactions =
                transactionRepository.count();

        long payments =
                paymentRepository.count();
        long pendingVisits =
                visitRepository
                        .countByStatut("DEMANDEE");


        long pendingAppointments =
                appointmentRepository
                        .countByStatut("DEMANDE");


        long activeTransactions =
                transactionRepository
                        .countByStatut("EN_NEGOCIATION");


        BigDecimal totalPayments =
                paymentRepository
                        .sumMontantByStatut("PAYE");

        return new AdminDashboardResponse(

                propertyRepository.count(),

                userRepository.count(),

                userRepository.countByRole_Code("CLIENT"),

                userRepository.countByRole_Code("AGENT"),

                visitRepository.count(),

                appointmentRepository.count(),

                transactionRepository.count(),

                paymentRepository.count(),

                pendingVisits,

                pendingAppointments,

                activeTransactions,

                totalPayments
        );
    }
    public List<PaymentResponse> findAllPayments() {

        return paymentRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toPaymentResponse)
                .toList();
    }
    private PaymentResponse toPaymentResponse(
            Payment payment
    ) {

        return new PaymentResponse(

                payment.getId(),

                payment.getTransaction().getId(),

                payment.getTransaction().getReference(),

                payment.getReference(),

                payment.getMontant(),

                payment.getDevise(),

                payment.getModePaiement(),

                payment.getStatut(),

                payment.getDatePaiement(),

                payment.getCreatedAt()
        );
    }
}