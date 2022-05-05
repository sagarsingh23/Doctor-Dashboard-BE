package com.dashboard.doctor_dashboard.entity.dtos;


import com.dashboard.doctor_dashboard.entity.Attributes;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@Getter
public class PatientDto extends PatientListDto {

    @JsonManagedReference
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Attributes attributes;


    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
