package com.dashboard.doctor_dashboard.entities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column
    private Boolean isBookedAgain;

    @Column
    private Long followUpAppointmentId;

    @PrePersist
    public void onCreate() {
        timestamp = new Date();
    }


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

