package com.hms.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String name;
    private String email;
    private LocalDate dob;
    private String gender;
    private String bloodGroup;
    private String phone;
    private String address;

    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
}
