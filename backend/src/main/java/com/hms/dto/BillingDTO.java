package com.hms.dto;

import com.hms.model.BillingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingDTO {
    private Long id;
    private Long appointmentId;
    private String patientName;
    private Double amount;
    private BillingStatus status;
    private LocalDateTime generatedAt;
}
