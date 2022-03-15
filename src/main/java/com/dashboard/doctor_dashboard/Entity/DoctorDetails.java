package com.dashboard.doctor_dashboard.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "doctor_details"
)
public class DoctorDetails {
    @Id
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name="age")
    private short age;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(name="specialty")
    private String speciality;

    private String phoneNo;

    //references

    @OneToMany(mappedBy = "doctorDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Patient> patient;
}
