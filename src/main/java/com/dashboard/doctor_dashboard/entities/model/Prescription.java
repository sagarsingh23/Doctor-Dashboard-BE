package com.dashboard.doctor_dashboard.entities.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "prescriptions",
        indexes = @Index(name = "index_appointId",columnList = "appointment_id")
)
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long presId;

    @Column(columnDefinition = "varchar(50)")
    private String drugName;
    private Long quantity;
    @Column(columnDefinition = "varchar(10)")
    private String type;
    private Long days;
    @Column(columnDefinition = "varchar(10)")
    private String time;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    @JsonBackReference("prescription")
    @ManyToOne()
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;


    public Prescription(String drugName, Long quantity, String type, Long days, String time) {
        this.drugName = drugName;
        this.quantity = quantity;
        this.type = type;
        this.days = days;
        this.time = time;
    }

    public Long getPresId() {
        return presId;
    }

    public void setPresId(Long presId) {
        this.presId = presId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }


    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "presId=" + presId +
                ", drugName='" + drugName + '\'' +
                ", quantity=" + quantity +
                ", type='" + type + '\'' +
                ", days=" + days +
                ", time='" + time + '\'' +
//                ", appointment=" + appointment +
                '}';
    }
}
