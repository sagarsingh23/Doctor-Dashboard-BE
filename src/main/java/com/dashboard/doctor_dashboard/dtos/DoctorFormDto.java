package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.enums.Category;
import com.dashboard.doctor_dashboard.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorFormDto {

    @NotNull
    private Long id;
    @Range(min = 24, max = 100, message = "Enter age between 24-100")
    private Short age;

    @Enumerated(EnumType.STRING)
    private Category speciality;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Pattern(regexp = "^([0-9]{10})", message = "Number should be of 10 digits")
    private String phoneNo;
    @NotNull
    private Short exp;
    @NotNull
    @NotEmpty(message = "degree can't be null")
    private String degree;
}
