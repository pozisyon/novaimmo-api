package com.novaimmo.demo.agent;

import com.novaimmo.demo.agent.dto.AgentDashboardResponse;
import com.novaimmo.demo.appointment.Appointment;
import com.novaimmo.demo.appointment.AppointmentRepository;
import com.novaimmo.demo.appointment.dto.AppointmentResponse;
import com.novaimmo.demo.auth.CurrentUserService;
import com.novaimmo.demo.transaction.TransactionRepository;
import com.novaimmo.demo.transaction.dto.TransactionResponse;
import com.novaimmo.demo.visit.PropertyVisit;
import com.novaimmo.demo.visit.PropertyVisitRepository;
import com.novaimmo.demo.transaction.Transaction;
import com.novaimmo.demo.visit.dto.PropertyVisitResponse;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;


@Service
public class AgentService {

    private final CurrentUserService currentUserService;
    private final PropertyVisitRepository visitRepository;
    private final AppointmentRepository appointmentRepository;
    private final TransactionRepository transactionRepository;

    public AgentService(
            CurrentUserService currentUserService,
            PropertyVisitRepository visitRepository,
            AppointmentRepository appointmentRepository,
            TransactionRepository transactionRepository
    ) {
        this.currentUserService =
                currentUserService;

        this.visitRepository =
                visitRepository;

        this.appointmentRepository =
                appointmentRepository;

        this.transactionRepository =
                transactionRepository;
    }


    public AgentDashboardResponse getDashboard() {

        Long agentId =
                currentUserService
                        .getCurrentUserId();


        long assignedVisits =
                visitRepository
                        .countByAgentId(
                                agentId
                        );


        long assignedAppointments =
                appointmentRepository
                        .countByAgentId(
                                agentId
                        );


        long assignedTransactions =
                transactionRepository
                        .countByAgentId(
                                agentId
                        );


        long activeTransactions =
                transactionRepository
                        .countByAgentIdAndStatut(
                                agentId,
                                "EN_NEGOCIATION"
                        );


        return new AgentDashboardResponse(

                assignedVisits,

                assignedAppointments,

                assignedTransactions,

                activeTransactions
        );
    }
    public List<PropertyVisitResponse> findMyVisits() {

        Long agentId =
                currentUserService
                        .getCurrentUserId();

        return visitRepository
                .findByAgentIdOrderByDateVisiteAsc(
                        agentId
                )
                .stream()
                .map(visit -> new PropertyVisitResponse(
                        visit.getId(),
                        visit.getProperty().getId(),
                        visit.getProperty().getReference(),
                        visit.getProperty().getTitre(),
                        visit.getClientId(),
                        visit.getAgentId(),
                        visit.getNomVisiteur(),
                        visit.getEmail(),
                        visit.getTelephone(),
                        visit.getDateVisite(),
                        visit.getNombrePersonnes(),
                        visit.getStatut(),
                        visit.getCommentaire(),
                        visit.getCreatedAt()
                ))
                .toList();
    }
    public List<AppointmentResponse> findMyAppointments() {

        Long agentId =
                currentUserService
                        .getCurrentUserId();

        return appointmentRepository
                .findByAgentIdOrderByDateDebutAsc(
                        agentId
                )
                .stream()
                .map(appointment ->
                        new AppointmentResponse(

                                appointment.getId(),

                                appointment.getClientId(),

                                appointment.getAgentId(),

                                appointment.getNomContact(),

                                appointment.getEmail(),

                                appointment.getTelephone(),

                                appointment.getSujet(),

                                appointment.getDateDebut(),

                                appointment.getDateFin(),

                                appointment.getLieu(),

                                appointment.getStatut(),

                                appointment.getNotes(),

                                appointment.getCreatedAt()
                        )
                )
                .toList();
    }

    public List<TransactionResponse> findMyTransactions() {

        Long agentId =
                currentUserService
                        .getCurrentUserId();


        return transactionRepository
                .findByAgentIdOrderByCreatedAtDesc(
                        agentId
                )
                .stream()
                .map(transaction ->
                        new TransactionResponse(

                                transaction.getId(),

                                transaction.getReference(),

                                transaction
                                        .getProperty()
                                        .getId(),

                                transaction
                                        .getProperty()
                                        .getReference(),

                                transaction
                                        .getProperty()
                                        .getTitre(),

                                transaction.getClientId(),

                                transaction.getAgentId(),

                                transaction.getTypeTransaction(),

                                transaction.getMontant(),

                                transaction.getDevise(),

                                transaction.getStatut(),

                                transaction.getDateTransaction(),

                                transaction.getNotes(),

                                transaction.getCreatedAt(),

                                transaction.getUpdatedAt()
                        )
                )
                .toList();
    }
    @Transactional
    public PropertyVisitResponse updateVisitStatus(
            Long visitId,
            String statut
    ) {

        Long agentId =
                currentUserService.getCurrentUserId();

        PropertyVisit visit =
                visitRepository
                        .findByIdAndAgentId(
                                visitId,
                                agentId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Visite introuvable ou non assignée à cet agent"
                                )
                        );

        String newStatus =
                statut.toUpperCase();

        if (!List.of(
                "DEMANDEE",
                "CONFIRMEE",
                "EFFECTUEE",
                "ANNULEE"
        ).contains(newStatus)) {

            throw new IllegalArgumentException(
                    "Statut de visite invalide"
            );
        }

        visit.setStatut(newStatus);

        PropertyVisit saved =
                visitRepository.save(visit);

        return toVisitResponse(saved);
    }
    private PropertyVisitResponse toVisitResponse(
            PropertyVisit visit
    ) {

        return new PropertyVisitResponse(

                visit.getId(),

                visit
                        .getProperty()
                        .getId(),

                visit
                        .getProperty()
                        .getReference(),

                visit
                        .getProperty()
                        .getTitre(),

                visit.getClientId(),

                visit.getAgentId(),

                visit.getNomVisiteur(),

                visit.getEmail(),

                visit.getTelephone(),

                visit.getDateVisite(),

                visit.getNombrePersonnes(),

                visit.getStatut(),

                visit.getCommentaire(),

                visit.getCreatedAt()
        );
    }
    @Transactional
    public AppointmentResponse updateAppointmentStatus(
            Long appointmentId,
            String statut
    ) {

        Long agentId =
                currentUserService
                        .getCurrentUserId();


        Appointment appointment =
                appointmentRepository
                        .findByIdAndAgentId(
                                appointmentId,
                                agentId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Rendez-vous introuvable ou non assigné à cet agent"
                                )
                        );


        String newStatus =
                statut.toUpperCase();


        if (!List.of(
                "DEMANDE",
                "CONFIRME",
                "TERMINE",
                "ANNULE"
        ).contains(newStatus)) {

            throw new IllegalArgumentException(
                    "Statut de rendez-vous invalide"
            );
        }


        appointment.setStatut(
                newStatus
        );


        Appointment saved =
                appointmentRepository
                        .save(appointment);


        return toAppointmentResponse(
                saved
        );
    }
    private AppointmentResponse toAppointmentResponse(
            Appointment appointment
    ) {

        return new AppointmentResponse(

                appointment.getId(),

                appointment.getClientId(),

                appointment.getAgentId(),

                appointment.getNomContact(),

                appointment.getEmail(),

                appointment.getTelephone(),

                appointment.getSujet(),

                appointment.getDateDebut(),

                appointment.getDateFin(),

                appointment.getLieu(),

                appointment.getStatut(),

                appointment.getNotes(),

                appointment.getCreatedAt()
        );
    }
    @Transactional
    public TransactionResponse updateTransactionStatus(
            Long transactionId,
            String statut
    ) {

        Long agentId =
                currentUserService
                        .getCurrentUserId();


        Transaction transaction =
                transactionRepository
                        .findByIdAndAgentId(
                                transactionId,
                                agentId
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Transaction introuvable ou non assignée à cet agent"
                                )
                        );


        String newStatus =
                statut.toUpperCase();


        if (!List.of(
                "EN_NEGOCIATION",
                "CONFIRMEE",
                "TERMINEE",
                "ANNULEE"
        ).contains(newStatus)) {

            throw new IllegalArgumentException(
                    "Statut de transaction invalide"
            );
        }


        transaction.setStatut(
                newStatus
        );


        Transaction saved =
                transactionRepository.save(
                        transaction
                );


        return toTransactionResponse(
                saved
        );
    }
    private TransactionResponse toTransactionResponse(
            Transaction transaction
    ) {

        return new TransactionResponse(

                transaction.getId(),

                transaction.getReference(),

                transaction
                        .getProperty()
                        .getId(),

                transaction
                        .getProperty()
                        .getReference(),

                transaction
                        .getProperty()
                        .getTitre(),

                transaction.getClientId(),

                transaction.getAgentId(),

                transaction.getTypeTransaction(),

                transaction.getMontant(),

                transaction.getDevise(),

                transaction.getStatut(),

                transaction.getDateTransaction(),

                transaction.getNotes(),

                transaction.getCreatedAt(),

                transaction.getUpdatedAt()
        );
    }
}