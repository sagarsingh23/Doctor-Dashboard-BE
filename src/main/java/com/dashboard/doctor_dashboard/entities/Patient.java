package com.dashboard.doctor_dashboard.entities;

import com.dashboard.doctor_dashboard.enums.BloodGroup;
import com.dashboard.doctor_dashboard.enums.Gender;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"squid:S5843","squid:S5869"})
@NoArgsConstructor
@Entity
@Table(
        name = "patient_details",
        indexes = @Index(name = "index_loginId",columnList = "login_id")
)
@SQLDelete(sql = "UPDATE patient_details SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Patient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pID;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10)")
    private Gender gender;


    @Positive(message = "Age can't be null or less than equal to 0")
    private int age;

    @NotEmpty
    @Size(min = 10, max = 10, message = "Phone Number should Contains only 10 digits")
    @Column(columnDefinition = "varchar(10)")
    private String mobileNo;


    @NotEmpty
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(3)")
    private BloodGroup bloodGroup;

    private String address;

    @Column(columnDefinition = "varchar(10)")
    private String alternateMobileNo;


    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    private boolean deleted = Boolean.FALSE;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;


    @PrePersist
    @PreUpdate
    public void onCreate() {
        timestamp = new Date();
    }


    @JsonManagedReference
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Appointment> appointments;


    @JsonBackReference()
    @OneToOne()
    @JoinColumn(name = "login_id",unique = true)
    private LoginDetails loginDetails;


    public Long getPID() {
        return pID;
    }

    public void setPID(Long pID) {
        this.pID = pID;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAlternateMobileNo() {
        return alternateMobileNo;
    }

    public void setAlternateMobileNo(String alternateMobileNo) {
        this.alternateMobileNo = alternateMobileNo;
    }

}
