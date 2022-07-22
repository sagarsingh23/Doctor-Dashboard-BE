package com.dashboard.doctor_dashboard.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PatientEntityDto {

    @NotNull
    @NotEmpty(message = "mobile number can't be empty")
    private String mobileNo;
    @NotNull
    @NotEmpty(message = "gender can't be empty")
    private String gender;
    @NotNull(message = "age can't be empty")
    private int age;
    @NotNull
    @NotEmpty(message = "blood group can't be empty")
    private String bloodGroup;
    @NotNull
    @NotEmpty(message = "address can't be empty")
    private String address;
    private String alternateMobileNo;

}
