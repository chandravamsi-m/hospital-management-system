package com.hms.controller;

import com.hms.dto.BillingDTO;
import com.hms.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public BillingDTO createInvoice(@RequestBody BillingDTO dto) {
        return billingService.createInvoice(dto);
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public BillingDTO payInvoice(@PathVariable Long id) {
        return billingService.payInvoice(id);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public List<BillingDTO> getBillingByPatient(@PathVariable Long patientId) {
        return billingService.getBillingByPatient(patientId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public BillingDTO getBillingById(@PathVariable Long id) {
        return billingService.getBillingById(id);
    }
}
