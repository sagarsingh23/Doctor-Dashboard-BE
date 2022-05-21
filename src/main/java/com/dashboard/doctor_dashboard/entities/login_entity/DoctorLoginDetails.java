package com.dashboard.doctor_dashboard.entities.login_entity;


import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.Patient;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "doctor_login_details", uniqueConstraints = @UniqueConstraint(columnNames = {"emailId"}))
public class DoctorLoginDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "firstName", nullable = false)
    private String firstName;
    private String lastName;
    @Column(name = "emailId", nullable = false, unique = true)
    private String emailId;
    @Column(name = "domain", nullable = false)
    private String domain;


    @OneToOne(mappedBy = "doctorLoginDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DoctorDetails doctorDetails;


    @JsonManagedReference
    @OneToOne(mappedBy = "doctorLoginDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Patient patient;



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
