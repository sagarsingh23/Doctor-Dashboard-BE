package com.dashboard.doctor_dashboard.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

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
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
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
    @Pattern(regexp = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$")
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
