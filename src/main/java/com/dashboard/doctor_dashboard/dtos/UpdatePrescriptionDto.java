package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.Prescription;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class UpdatePrescriptionDto {

    @NotNull(message = "patient details can't be empty")
    private PatientDto patientDto;

    private List<Prescription> prescriptions;

    @NotNull
    @NotEmpty(message = "status can't be empty")
    private String status;


    @NotNull
    @NotEmpty(message = "notes can't be empty")
    private String notes;

    private Boolean isBookedAgain;

    private Long followUpAppointmentId;

    public Boolean getBookedAgain() {
        return isBookedAgain;
    }

    public void setBookedAgain(Boolean bookedAgain) {
        isBookedAgain = bookedAgain;
    }

    public Long getFollowUpAppointmentId() {
        return followUpAppointmentId;
    }

    public void setFollowUpAppointmentId(Long followUpAppointmentId) {
        this.followUpAppointmentId = followUpAppointmentId;
    }

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
