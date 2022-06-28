package com.dashboard.doctor_dashboard.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "appointments"
)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointId;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^((?i)Orthologist|Dentist|General|Gastrologist|Dermatologist)", message = "Select from specified speciality [Orthologist,Dentist,Dermatologist,General,Gastrologist]")
    private String category;

    @NotNull
    @Future(message = "Only future dates can be entered ")
    private LocalDate dateOfAppointment;


    @Size(max = 100)
    private String symptoms;
    @NotNull
    @NotEmpty
    private String patientName;
    @NotNull
    @NotEmpty
    private String patientEmail;
    @NotNull
    @NotEmpty
    private String doctorName;
    @NotNull
    private LocalTime appointmentTime;
    @Column(name = "is_read",columnDefinition = "boolean default 0")
    private Boolean isRead;
    @NotNull
    @NotEmpty
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date timestamp;

    @Column
    private Boolean isBookedAgain;

    @Column
    private Long followUpAppointmentId;

    @PrePersist
    public void onCreate() {
        timestamp = new Date();
    }


    //private UUID referenceId;
    @NotNull
    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @NotNull
    @ManyToOne()
    @JsonBackReference("value_doctor")
    @JoinColumn(name = "doctor_id")
    private DoctorDetails doctorDetails;

    @JsonManagedReference
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Attributes attributes;


    @JsonManagedReference("prescription")
    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prescription> prescription;



    public Long getAppointId() {
        return appointId;
    }

    public void setAppointId(Long appointId) {
        this.appointId = appointId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDateOfAppointment() {
        return dateOfAppointment;
    }

    public void setDateOfAppointment(LocalDate dateOfAppointment) {
        this.dateOfAppointment = dateOfAppointment;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public DoctorDetails getDoctorDetails() {
        return doctorDetails;
    }

    public void setDoctorDetails(DoctorDetails doctorDetails) {
        this.doctorDetails = doctorDetails;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsBookedAgain() {
        return isBookedAgain;
    }

    public void setIsBookedAgain(Boolean bookedAgain) {
        isBookedAgain = bookedAgain;
    }

    public Long getFollowUpAppointmentId() {
        return followUpAppointmentId;
    }

    public void setFollowUpAppointmentId(Long followUpAppointmentId) {
        this.followUpAppointmentId = followUpAppointmentId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointId=" + appointId +
                ", category='" + category + '\'' +
                ", dateOfAppointment=" + dateOfAppointment +
                ", symptoms='" + symptoms + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientEmail='" + patientEmail + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", appointmentTime=" + appointmentTime +
                ", isRead=" + isRead +
                ", status='" + status + '\'' +
                ", timestamp=" + timestamp +
                ", isBookedAgain=" + isBookedAgain +
                ", followUpAppointmentId=" + followUpAppointmentId +
                ", patient=" + patient +
                ", doctorDetails=" + doctorDetails +
                ", attributes=" + attributes +
                ", prescription=" + prescription +
                '}';
    }
}

