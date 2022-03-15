package com.dashboard.doctor_dashboard.Entity;

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
    @Column(name = "id")
    private Long pID;
    private String fullName;
    private String emailId;
    private LocalDate lastVisitedDate;
    private String gender;
    private int age;
    private String category;
    private String mobileNo;

    @JsonManagedReference
    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Attributes attributes;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name="doctor_id",nullable = false)
    private DoctorDetails doctorDetails;

}
