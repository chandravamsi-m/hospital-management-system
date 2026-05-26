package com.hms.service;

import com.hms.dto.AppointmentDTO;
import com.hms.model.Appointment;
import com.hms.model.AppointmentStatus;
import com.hms.model.Doctor;
import com.hms.model.Patient;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.DoctorRepository;
import com.hms.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    public AppointmentDTO createAppointment(AppointmentDTO dto) {
        // Validate doctor and patient exist
        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + dto.getDoctorId()));
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found with ID: " + dto.getPatientId()));

        // Check for schedule clashes (double bookings for the doctor at that exact time)
        List<Appointment> conflicts = appointmentRepository.findByDoctorIdAndAppointmentTime(dto.getDoctorId(), dto.getAppointmentTime());
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Doctor is already booked at: " + dto.getAppointmentTime());
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setReason(dto.getReason());
        // Default to PENDING status upon creation
        appointment.setStatus(AppointmentStatus.PENDING);

        appointment = appointmentRepository.save(appointment);
        return mapToDTO(appointment);
    }

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO getAppointmentDTOById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        return appointment != null ? mapToDTO(appointment) : null;
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public List<AppointmentDTO> getAppointmentsByDoctorId(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByPatientId(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + id));
        appointment.setStatus(status);
        appointment = appointmentRepository.save(appointment);
        return mapToDTO(appointment);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    private AppointmentDTO mapToDTO(Appointment appointment) {
        return new AppointmentDTO(
                appointment.getId(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getPatient() != null ? appointment.getPatient().getName() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getId() : null,
                appointment.getDoctor() != null ? appointment.getDoctor().getName() : null,
                appointment.getAppointmentTime(),
                appointment.getReason(),
                appointment.getStatus()
        );
    }
}
