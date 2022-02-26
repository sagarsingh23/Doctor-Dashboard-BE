package com.dashboard.doctor_dashboard.Entity.doctor_entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "doctor_details"
)
public class DoctorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name="age",nullable = false)
    private short age;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(name="specialty",nullable = false)
    private String speciality;

    //references
}
