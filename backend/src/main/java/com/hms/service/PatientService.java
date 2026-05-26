package com.hms.service;

import com.hms.dto.PatientDTO;
import com.hms.model.Patient;
import com.hms.model.Role;
import com.hms.model.User;
import com.hms.repository.PatientRepository;
import com.hms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PatientDTO createPatient(PatientDTO dto) {
        // Create User login profile first
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode("PatTempPass123!"));
        user.setRole(Role.PATIENT);
        user = userRepository.save(user);

        // Create Patient demographic record
        Patient patient = new Patient();
        patient.setUser(user);
        patient.setName(dto.getName());
        patient.setEmail(dto.getEmail());
        patient.setDob(dto.getDob());
        patient.setGender(dto.getGender());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setPhone(dto.getPhone());
        patient.setAddress(dto.getAddress());
        patient.setMedicalHistory(dto.getMedicalHistory());

        patient = patientRepository.save(patient);
        return mapToDTO(patient);
    }

    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PatientDTO getPatientDTOById(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        return patient != null ? mapToDTO(patient) : null;
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public PatientDTO updatePatient(Long id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient != null) {
            patient.setName(dto.getName());
            patient.setDob(dto.getDob());
            patient.setGender(dto.getGender());
            patient.setBloodGroup(dto.getBloodGroup());
            patient.setPhone(dto.getPhone());
            patient.setAddress(dto.getAddress());
            patient.setMedicalHistory(dto.getMedicalHistory());

            User user = patient.getUser();
            if (user != null) {
                user.setName(dto.getName());
                userRepository.save(user);
            }

            patient = patientRepository.save(patient);
            return mapToDTO(patient);
        }
        return null;
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    private PatientDTO mapToDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getUser() != null ? patient.getUser().getId() : null,
                patient.getName(),
                patient.getEmail(),
                patient.getDob(),
                patient.getGender(),
                patient.getBloodGroup(),
                patient.getPhone(),
                patient.getAddress(),
                patient.getMedicalHistory()
        );
    }
}
