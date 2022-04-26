package com.dashboard.doctor_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;


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
    @Column(name = "last_name")
    private String lastName;
    @Column(name="age")
    @Range(min = 24,max = 100,message = "enter age between 24-100")
    private Short age;
    @Column(name = "email",nullable = false,unique = true)
    private String email;
    @Column(name="speciality")
    private String speciality;
    private String phoneNo;

    private String gender;
    //references
    @JsonManagedReference
    @OneToMany(mappedBy = "doctorDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Patient> patient;


    @JsonManagedReference
    @OneToMany(mappedBy = "doctorDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Todolist> todolist;


    public void setId(Long id) {
        this.id = id;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
