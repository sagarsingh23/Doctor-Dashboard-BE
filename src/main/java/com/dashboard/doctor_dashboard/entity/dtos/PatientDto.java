package com.dashboard.doctor_dashboard.entity.dtos;

import com.dashboard.doctor_dashboard.entity.Attributes;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PatientDto extends PatientListDto {

    private Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public PatientDto(Long pID, String fullName, String emailId, String status, String category, LocalDate lastVisitedDate, String mobileNo, String gender, int age,Attributes attributes) {

        super(pID,fullName,emailId,status,category,lastVisitedDate,mobileNo,gender,age);
        this.attributes=attributes;
    }

    public void setAttributes(Attributes attributes) {

        this.attributes = attributes;
    }
}
