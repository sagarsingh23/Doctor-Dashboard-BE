package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "doctor_details",
        indexes = @Index(name = "index_speciality",columnList = "speciality")
)
@SQLDelete(sql = "UPDATE doctor_details SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DoctorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age",nullable = false)
    @Range(min = 24, max = 100, message = "enter age between 24-100")
    private Short age;

    @Column(name = "speciality",columnDefinition = "varchar(20)",nullable = false)
    @Enumerated(EnumType.STRING)
    private Category speciality;

    @Column(name = "phone_no",columnDefinition = "varchar(10)",nullable = false)
    private String phoneNo;

    @Column(name = "gender",nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "experience",nullable = false)
    private short exp;

    @Column(name = "degree",columnDefinition = "varchar(250)",nullable = false)
    private String degree;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    private boolean deleted = Boolean.FALSE;



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

    public DoctorDetails(Short age, Category speciality, String phoneNo, Gender gender, short exp, String degree, Long loginId) {
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

    public Category getSpeciality() {
        return speciality;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public Gender getGender() {
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

