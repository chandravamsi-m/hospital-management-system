package com.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private LocalDate dob;
    private String gender;
    private String bloodGroup;
    private String phone;
    private String address;
    private String medicalHistory;
}
