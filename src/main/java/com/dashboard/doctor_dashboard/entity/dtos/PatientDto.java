package com.dashboard.doctor_dashboard.entity.dtos;

import com.dashboard.doctor_dashboard.entity.Attributes;

import lombok.Getter;

@Getter
public class PatientDto extends PatientListDto {

    private Attributes attributes;

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
