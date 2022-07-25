package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.model.Patient;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface PatientRepository extends PagingAndSortingRepository<Patient, Long> {

    @Query(value = "select * from patient_details where deleted = false and login_id=:loginId", nativeQuery = true)
    Patient getPatientByLoginId(Long loginId);

    @Query(value = "select id from patient_details where deleted = false and login_id=:loginId ", nativeQuery = true)
    Long getId(Long loginId);

    @Query(value = "insert into patient_details (age,mobile_no,alternate_mobile_no,timestamp,gender,address,blood_group,login_id,created_at,deleted) values(:age,:mobileNo,:alternateMobileNo,now(),:gender,:address,:bloodGroup,:loginId,now(),false)",nativeQuery = true)
    @Transactional
    @Modifying
    void insertIntoPatient(int age,String mobileNo,String alternateMobileNo,String gender,String address,String bloodGroup,Long loginId);

    @Query(value = "update patient_details set mobile_no=:mobileNo where deleted = false and id=:patientId",nativeQuery = true)
    @Modifying
    @Transactional
    void updateMobileNo(String mobileNo,long patientId);
}
