package com.dashboard.doctor_dashboard.Entity.dtos;


import com.dashboard.doctor_dashboard.Entity.Attributes;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDate;


@Data
public class PatientDto {

    private Long pID;
    private String fullName;
    private String emailId;
    private LocalDate lastVisitedDate;
    private String gender;
    private int age;
    private String category;
    private String mobileNo;
    private String status;

    @JsonManagedReference
    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Attributes attributes;

}
