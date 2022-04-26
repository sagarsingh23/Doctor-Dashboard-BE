package com.dashboard.doctor_dashboard.jwt.Entity;


import lombok.NoArgsConstructor;

import java.util.Objects;


@NoArgsConstructor
public class DoctorClaims {
    public Long doctorId;
    public String doctorName;
    public String doctorEmail;



    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }



    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }


    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorClaims that = (DoctorClaims) o;
        return doctorId.equals(that.doctorId) && doctorName.equals(that.doctorName) && doctorEmail.equals(that.doctorEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, doctorName, doctorEmail);  //NOSONAR
    }
}
