package com.dashboard.doctor_dashboard.Repository;

import com.dashboard.doctor_dashboard.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
    @Query(value = "select * from patients where doctor_id = :doctorId",nativeQuery = true)
    List<Patient> getAllPatientByDoctorId(@Param(value = "doctorId") Long doctorId);

    @Query(value = "update patients set status =:status where id=:patientId ",nativeQuery = true)
    @Modifying
    @Transactional
    void changePatientStatus(Long patientId,String status);


    @Query(value = "select id from patients where id=:patientId ",nativeQuery = true)
    Long getId(Long patientId);

    @Query(value = "select * from patients where doctor_id = :doctorId order by id desc limit 3",nativeQuery = true)
    List<Patient> recentlyAddedPatient(Long doctorId);




    //Dashboard Chart

    @Query(value = "Select COUNT(id) from patients where doctor_id =:doctorId",nativeQuery = true)
    int totalNoOfPatient(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select upper(category),count(category) from patients where doctor_id = :doctorId group by category",nativeQuery = true)
    ArrayList<String> patientCategory(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select upper(gender),count(gender) from patients where doctor_id =:doctorId group by gender",nativeQuery = true)
    ArrayList<String> gender(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select upper(status),count(status) from patients where doctor_id =:doctorId group by status",nativeQuery = true)
    ArrayList<String> activePatient(@Param(value = "doctorId") Long doctorId);

    @Query(value = "select upper(blood_group),count(blood_group) from attributes inner join patients where patients.id = attributes.id and doctor_id =:doctorId group by blood_group",nativeQuery = true)
    ArrayList<String> bloodGroup(@Param(value = "doctorId") Long doctorId);

    @Query(value = "SELECT a.age,count(*) as count from " +
            "(select case when age <=2 then \"0-2\" when age >=3 and age <=14 then \"3-14\" " +
            "when age >=15 and age <=25 then \"15-25\" when age >=26 and age <=64 then \"26-64\" " +
            "when age >=65 then \"65+\" end age FROM patients where doctor_id =:doctorId)  a  group by a.age",nativeQuery = true)
    ArrayList<String> ageChart(@Param(value = "doctorId") Long doctorId);




    //Add-On feature Refer Patient


    @Query(value = "select first_name from doctor_details where id = (select doctor_id from patients where id =:patientId)",nativeQuery = true)
    String findDoctorNameByPatientId(Long patientId);

    @Query(value = "select full_name from patients where id =:patientId",nativeQuery = true)
    String findPatientNameByPatientId(Long patientId);


    @Query(value = "update patients set doctor_id=:doctorId, is_changed=true ,message ='Dr. ' :docName ' refered ' :patientName  ' to you'  where id=:patientId",nativeQuery = true)
    @Modifying
    @Transactional
    void referPatients(Long doctorId,Long patientId,String docName,String patientName);


    @Query(value = "select message from patients where doctor_id = :doctorId and is_changed = true",nativeQuery = true)
    ArrayList<String> getMessageForReferredPatient(Long doctorId);


    @Query(value = "update patients set is_changed = false where doctor_id =:doctorId",nativeQuery = true)
    @Modifying
    @Transactional
    void changeStatus(Long doctorId);

}
