package com.hms.service;

import com.hms.dto.BillingDTO;
import com.hms.model.Appointment;
import com.hms.model.Billing;
import com.hms.model.BillingStatus;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public BillingDTO createInvoice(BillingDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + dto.getAppointmentId()));

        Billing billing = new Billing();
        billing.setAppointment(appointment);
        billing.setAmount(dto.getAmount());
        billing.setStatus(BillingStatus.UNPAID);
        billing.setGeneratedAt(LocalDateTime.now());

        billing = billingRepository.save(billing);
        return mapToDTO(billing);
    }

    public BillingDTO payInvoice(Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Billing record not found with ID: " + id));
        billing.setStatus(BillingStatus.PAID);
        billing = billingRepository.save(billing);
        return mapToDTO(billing);
    }

    public List<BillingDTO> getBillingByPatient(Long patientId) {
        return billingRepository.findByAppointmentPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public BillingDTO getBillingById(Long id) {
        Billing billing = billingRepository.findById(id).orElse(null);
        return billing != null ? mapToDTO(billing) : null;
    }

    private BillingDTO mapToDTO(Billing billing) {
        return new BillingDTO(
                billing.getId(),
                billing.getAppointment() != null ? billing.getAppointment().getId() : null,
                billing.getAppointment() != null && billing.getAppointment().getPatient() != null ? billing.getAppointment().getPatient().getName() : null,
                billing.getAmount(),
                billing.getStatus(),
                billing.getGeneratedAt()
        );
    }
}
