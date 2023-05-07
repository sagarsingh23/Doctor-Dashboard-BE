package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.enums.Category;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
public class DoctorListDto {
    private long id;
    private String name;
    private String email;
    private String profilePic;
    private Category speciality;
    private short exp;
    private String degree;


    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Category getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Category speciality) {
        this.speciality = speciality;
    }

    public short getExp() {
        return exp;
    }

    public void setExp(short exp) {
        this.exp = exp;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
