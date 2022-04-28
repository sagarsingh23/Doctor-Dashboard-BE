package com.dashboard.doctor_dashboard.entity.dtos;

import java.time.LocalDate;


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


    public Long getPID() {
        return pID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFullName() {
        return fullName;
    }

    public void setPID(Long pID) {
        this.pID = pID;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setLastVisitedDate(LocalDate lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public void setAge(int age) {
        this.age = age;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
