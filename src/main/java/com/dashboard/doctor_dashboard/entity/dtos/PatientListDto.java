package com.dashboard.doctor_dashboard.entity.dtos;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
public class PatientListDto {
    private Long pID;
    private String fullName;
    private String emailId;
    private String status;
    private String category;
    private LocalDate lastVisitedDate;
    private String mobileNo;
    private String gender;
    private int age;

    public PatientListDto(Long pID, String fullName, String emailId, String status, String category, LocalDate lastVisitedDate, String mobileNo, String gender, int age) {
        this.pID = pID;
        this.fullName = fullName;
        this.emailId = emailId;
        this.status = status;
        this.category = category;
        this.lastVisitedDate = lastVisitedDate;
        this.mobileNo = mobileNo;
        this.gender = gender;
        this.age = age;
    }


    public void setPID(Long pID) {
        this.pID = pID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public String getLastVisitedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return lastVisitedDate.format(formatter);
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLastVisitedDate(LocalDate lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
