package com.hms.controller;

import com.hms.dto.PatientDTO;
import com.hms.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public PatientDTO createPatient(@RequestBody PatientDTO patientDTO) {
        return patientService.createPatient(patientDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public List<PatientDTO> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PATIENT')")
    public PatientDTO getPatient(@PathVariable Long id) {
        return patientService.getPatientDTOById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        return patientService.updatePatient(id, patientDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}
