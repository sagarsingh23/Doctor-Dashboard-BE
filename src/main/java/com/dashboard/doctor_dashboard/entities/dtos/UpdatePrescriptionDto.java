package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UpdatePrescriptionDto {
    private PatientDto patientDto;
    private List<Prescription> prescriptions;
    private String status;
    private String notes;

    public PatientDto getPatientDto() {
        return patientDto;
    }

    public void setPatientDto(PatientDto patientDto) {
        this.patientDto = patientDto;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
