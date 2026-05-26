package com.hms.controller;

import com.hms.dto.DoctorDTO;
import com.hms.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorDTO createDoctor(@RequestBody DoctorDTO doctorDTO) {
        return doctorService.createDoctor(doctorDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT', 'RECEPTIONIST')")
    public List<DoctorDTO> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT', 'RECEPTIONIST')")
    public DoctorDTO getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorDTOById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorDTO updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        return doctorService.updateDoctor(id, doctorDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}

