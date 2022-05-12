package com.dashboard.doctor_dashboard.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

@SuppressWarnings({"squid:S5843","squid:S5869"})
@NoArgsConstructor
@Entity
@Table(
        name = "patients"
)
public class Patient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pID;

    @NotEmpty
    @Size(min = 3, max = 100, message = "Name field should contain Atleast 3 character")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "full name should only contain characters")
    private String fullName;


    @Email(message = "Email is not valid",
            regexp = "^[A-Za-z0-9._]{3,}@[A_Za-z0-9]{3,}[.]{1}[A-Za-z0-9.]{2,6}$",
            flags = Pattern.Flag.CASE_INSENSITIVE)                               //NOSONAR
    @NotEmpty
    private String emailId;


    @NotNull
    @PastOrPresent
    private LocalDate lastVisitedDate;

    @NotEmpty
    @Pattern(regexp = "^(Male|Female|Others)",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Enter Correct Gender!!")
    private String gender;


    @Positive(message = "age can't be null or less than equal to 0")
    private int age;

    @NotEmpty
    @Pattern(regexp = "^(General|Gastrology|Neurology|Orthology|Cardiology)",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Category Should be Amongst these choices:" +
                    "General,Gastrology,Neurology,Orthology,Cardiology")
    private String category;

    @NotEmpty
    @Size(min = 10, max = 10, message = "phone Number should Contains only 10 digits")
    private String mobileNo;


    private String status;
    private boolean isChanged;
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    @PrePersist
    @PreUpdate
    public void onCreate() {
        timestamp = new Date();
    }

    @JsonManagedReference
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Attributes attributes;


    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "doctor_id")
    private DoctorDetails doctorDetails;


    public Long getPID() {
        return pID;
    }

    public void setPID(Long pID) {
        this.pID = pID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public LocalDate getLastVisitedDate() {
        return lastVisitedDate;
    }

    public void setLastVisitedDate(LocalDate lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void setDoctorDetails(DoctorDetails doctorDetails) {
        this.doctorDetails = doctorDetails;
    }
}
