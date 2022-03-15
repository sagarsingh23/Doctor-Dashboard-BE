package com.dashboard.doctor_dashboard.Repository;

import com.dashboard.doctor_dashboard.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    @Query(value = "select * from patients where doctor_id = :doctorId",nativeQuery = true)
    List<Patient> getAllPatientByDoctorId(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select COUNT(id) from patients where doctor_id =:doctorId",nativeQuery = true)
    int totalNoOfPatient(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select category,count(category) from patients where doctor_id = :doctorId group by category",nativeQuery = true)
    ArrayList<String> patientCategory(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select gender,count(gender) from patients where doctor_id =:doctorId group by gender",nativeQuery = true)
    ArrayList<String> gender(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select status,count(status) from patients where doctor_id =:doctorId group by status",nativeQuery = true)
    ArrayList<String> activePatient(@Param(value = "doctorId") Long doctorId);


    @Query(value = "select upper(blood_group),count(blood_group) from attributes inner join patients where patients.id = attributes.id and doctor_id =:doctorId group by blood_group",nativeQuery = true)
    ArrayList<String> bloodGroup(@Param(value = "doctorId") Long doctorId);

}
