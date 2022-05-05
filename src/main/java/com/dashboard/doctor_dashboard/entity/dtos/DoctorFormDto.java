package com.dashboard.doctor_dashboard.entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@Getter
public class DoctorFormDto {

    @NotNull
    private long id;
    @Range(min = 24, max = 100, message = "enter age between 24-100")
    private Short age;
    @Pattern(regexp = "^((?i)orthology|neurology|general|gastrology|cardiology)", message = "select from specified speciality [orthology,neurology,cardiology,general,gastrology]")
    private String speciality;
    @Pattern(regexp = "^((?i)male|female|others)", message = "select from specified gender [male,female,others]")
    private String gender;
    @Pattern(regexp = "^([0-9]{10})", message = "number should be of 10 digits")
    private String phoneNo;

    public long getId() {
        return id;
    }

    public Short getAge() {
        return age;
    }


    public String getSpeciality() {
        return speciality;
    }

    public String getGender() {
        return gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }
}
