package com.dashboard.doctor_dashboard.Entity.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class DoctorFormDto {



    public DoctorFormDto(long id, Short age, String speciality, String gender, String phoneNo) {
        this.id = id;
        this.age = age;
        this.speciality = speciality;
        this.gender = gender;
        this.phoneNo = phoneNo;
    }

    @NotNull
    private long id;
    @Range(min = 24,max = 100,message = "enter age between 24-100")
    private Short age;
    @Pattern(regexp = "^((?i)orthology|neurology|general|gastrology)" ,message ="select from specified speciality [orthology,neurology,general,gastrology]")
    private String speciality;
    @Pattern(regexp= "^((?i)male|female|others)", message = "select from specified gender [male,female,others]")
    private String gender;
    @Pattern(regexp = "^([0-9]{10})",message = "number should be of 10 digits")
    private String phoneNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
