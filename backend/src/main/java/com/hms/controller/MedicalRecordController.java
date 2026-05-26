package com.hms.controller;

import com.hms.dto.MedicalRecordDTO;
import com.hms.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public MedicalRecordDTO createMedicalRecord(@RequestBody MedicalRecordDTO dto) {
        return medicalRecordService.createMedicalRecord(dto);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public List<MedicalRecordDTO> getRecordsByPatient(@PathVariable Long patientId) {
        return medicalRecordService.getMedicalRecordsByPatient(patientId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public MedicalRecordDTO getRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }
}
