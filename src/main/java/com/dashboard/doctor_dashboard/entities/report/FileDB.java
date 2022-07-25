package com.dashboard.doctor_dashboard.entities.report;


import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "patient_reports")
public class FileDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
    private String name;
    private String type;
    @Lob
    @NotNull
    private byte[] dataReport;

    @Column(unique = true)
    private Long appointmentId;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;


    public FileDB() {
    }

    public FileDB(String name, String type,  byte[] dataReport, Long appointmentId) {
        this.name = name;
        this.type = type;
        this.dataReport = dataReport;
        this.appointmentId = appointmentId;
    }


    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getDataReport() {
        return dataReport;
    }

    public void setDataReport(byte[] dataReport) {
        this.dataReport = dataReport;
    }

}