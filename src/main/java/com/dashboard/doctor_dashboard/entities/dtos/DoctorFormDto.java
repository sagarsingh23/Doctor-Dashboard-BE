package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@AllArgsConstructor
public class DoctorFormDto {

    @NotNull
    private Long id;
    @Range(min = 24, max = 100, message = "Enter age between 24-100")
    private Short age;
    @Pattern(regexp = "^((?i)Orthologist|Dentist|General|Gastrologist|Dermatologist)", message = "Select from specified speciality [Orthologist,Dentist,Dermatologist,General,Gastrologist]")
    private String speciality;
    @Pattern(regexp = "^((?i)male|female|others)", message = "Select from specified gender [Male,Female,Others]")
    private String gender;
    @Pattern(regexp = "^([0-9]{10})", message = "Number should be of 10 digits")
    private String phoneNo;
    @NotNull
    private Short exp;
    @NotNull
    @NotEmpty
    private String degree;

}
