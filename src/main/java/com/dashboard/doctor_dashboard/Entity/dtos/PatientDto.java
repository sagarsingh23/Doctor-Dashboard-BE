package com.dashboard.doctor_dashboard.Entity.dtos;


import com.dashboard.doctor_dashboard.Entity.Attributes;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;


public class PatientDto extends PatientListDto {

    @JsonManagedReference
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Attributes attributes;


    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }
}
