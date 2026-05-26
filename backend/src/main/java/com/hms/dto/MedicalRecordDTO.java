package com.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDTO {
    private Long id;
    private Long appointmentId;
    private String patientName;
    private String doctorName;
    private String diagnosis;
    private String prescription;
    private String doctorNotes;
    private LocalDateTime createdAt;
}
