package com.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String specialization;
    private int experience;
    private String qualification;
    private String department;
}
