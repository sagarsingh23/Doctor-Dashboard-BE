package com.dashboard.doctor_dashboard.entities.model;

import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "doctor_details"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DoctorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age",nullable = false)
    @Range(min = 24, max = 100, message = "enter age between 24-100")
    private Short age;

    @Column(name = "speciality",nullable = false)
    private String speciality;

    @Column(name = "phone_no",nullable = false)
    private String phoneNo;

    @Column(name = "gender",nullable = false)
    private String gender;

    @Column(name = "experience",nullable = false)
    private short exp;

    @Column(name = "degree",nullable = false)
    private String degree;


    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;



    //references
    @JsonManagedReference("value_doctor")
    @OneToMany(mappedBy = "doctorDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;


    @JsonManagedReference
    @OneToMany(mappedBy = "doctorDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Todolist> todolist;


    @OneToOne()
    @JoinColumn(name = "login_id",referencedColumnName = "id",nullable = false)
    private LoginDetails loginDetails;

    @Column(name = "login_id", insertable=false, updatable=false,nullable = false)
    private Long loginId;



    public Long getLoginId() {
        return loginId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DoctorDetails(Short age, String speciality, String phoneNo, String gender, short exp, String degree, Long loginId) {
        this.age = age;
        this.speciality = speciality;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.exp = exp;
        this.degree = degree;
        this.loginId = loginId;
    }

    public Long getId() {
        return id;
    }

    public Short getAge() {
        return age;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getGender() {
        return gender;
    }


    @Override
    public String toString() {
        return "DoctorDetails{" +
                "id=" + id +
                ", age=" + age +
                ", speciality='" + speciality + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", gender='" + gender + '\'' +
                ", exp=" + exp +
                ", degree='" + degree + '\'' +
                ", loginId=" + loginId +
                '}';
    }
}

