package com.hms.service;

import com.hms.dto.MedicalRecordDTO;
import com.hms.model.Appointment;
import com.hms.model.AppointmentStatus;
import com.hms.model.MedicalRecord;
import com.hms.repository.AppointmentRepository;
import com.hms.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO dto) {
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + dto.getAppointmentId()));

        MedicalRecord record = new MedicalRecord();
        record.setAppointment(appointment);
        record.setDiagnosis(dto.getDiagnosis());
        record.setPrescription(dto.getPrescription());
        record.setDoctorNotes(dto.getDoctorNotes());
        record.setCreatedAt(LocalDateTime.now());

        // Automatically complete the appointment when a medical record is compiled
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        record = medicalRecordRepository.save(record);
        return mapToDTO(record);
    }

    public List<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId) {
        return medicalRecordRepository.findByAppointmentPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MedicalRecordDTO getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id).orElse(null);
        return record != null ? mapToDTO(record) : null;
    }

    private MedicalRecordDTO mapToDTO(MedicalRecord record) {
        return new MedicalRecordDTO(
                record.getId(),
                record.getAppointment() != null ? record.getAppointment().getId() : null,
                record.getAppointment() != null && record.getAppointment().getPatient() != null ? record.getAppointment().getPatient().getName() : null,
                record.getAppointment() != null && record.getAppointment().getDoctor() != null ? record.getAppointment().getDoctor().getName() : null,
                record.getDiagnosis(),
                record.getPrescription(),
                record.getDoctorNotes(),
                record.getCreatedAt()
        );
    }
}
