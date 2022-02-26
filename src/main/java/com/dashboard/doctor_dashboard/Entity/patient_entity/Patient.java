package com.dashboard.doctor_dashboard.Entity.patient_entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "patients"
)
public class Patient {

    @Id
    @GeneratedValue(generator = "patient_sequence")
    @SequenceGenerator(name = "patient_sequence",initialValue = 1,allocationSize = 100)
    @Column(name = "id")
    private Long pID;
    private String fullName;
    private String emailId;
    private LocalDate dateOfBirth;
    private String gender;
    private int age;

    @JsonManagedReference
    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Attributes attributes;

}
