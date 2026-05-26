package com.hms.service;

import com.hms.dto.DoctorDTO;
import com.hms.model.Doctor;
import com.hms.model.Role;
import com.hms.model.User;
import com.hms.repository.DoctorRepository;
import com.hms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DoctorDTO createDoctor(DoctorDTO dto) {
        // Create User profile first
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        // Standard initial password, should be changed upon login
        user.setPassword(passwordEncoder.encode("DocTempPass123!"));
        user.setRole(Role.DOCTOR);
        user = userRepository.save(user);

        // Create Doctor profile
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setName(dto.getName());
        doctor.setEmail(dto.getEmail());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setExperience(dto.getExperience());
        doctor.setQualification(dto.getQualification());
        doctor.setDepartment(dto.getDepartment());

        doctor = doctorRepository.save(doctor);
        return mapToDTO(doctor);
    }

    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DoctorDTO getDoctorDTOById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        return doctor != null ? mapToDTO(doctor) : null;
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public DoctorDTO updateDoctor(Long id, DoctorDTO dto) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);
        if (doctor != null) {
            doctor.setName(dto.getName());
            doctor.setSpecialization(dto.getSpecialization());
            doctor.setExperience(dto.getExperience());
            doctor.setEmail(dto.getEmail());
            doctor.setQualification(dto.getQualification());
            doctor.setDepartment(dto.getDepartment());
            
            // Also sync user profile
            User user = doctor.getUser();
            if (user != null) {
                user.setName(dto.getName());
                user.setEmail(dto.getEmail());
                userRepository.save(user);
            }
            
            doctor = doctorRepository.save(doctor);
            return mapToDTO(doctor);
        }
        return null;
    }

    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    private DoctorDTO mapToDTO(Doctor doctor) {
        return new DoctorDTO(
                doctor.getId(),
                doctor.getUser() != null ? doctor.getUser().getId() : null,
                doctor.getName(),
                doctor.getEmail(),
                doctor.getSpecialization(),
                doctor.getExperience(),
                doctor.getQualification(),
                doctor.getDepartment()
        );
    }
}

