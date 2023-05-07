package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.enums.BloodGroup;
import com.dashboard.doctor_dashboard.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotNull(message = "age can't be empty")
    private int age;
    @NotNull
    @Enumerated(EnumType.STRING)
    private BloodGroup bloodGroup;
    @NotNull
    @NotEmpty(message = "address can't be empty")
    private String address;
    private String alternateMobileNo;
}
