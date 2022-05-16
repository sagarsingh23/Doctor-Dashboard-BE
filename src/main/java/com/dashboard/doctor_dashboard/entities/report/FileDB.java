package com.dashboard.doctor_dashboard.entities.report;


import javax.persistence.*;

@Entity
@Table(name = "files")
public class FileDB {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] dataReport;

    @Column(unique = true)
    private Long patientId;

    public FileDB() {
    }


    public FileDB(String name, String type, byte[] dataReport, Long patientId) {
        this.name = name;
        this.type = type;
        this.dataReport = dataReport;
        this.patientId = patientId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
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