package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorDropdownDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorDetails, Long> {


    @Query(value = "update DoctorDetails set phoneNo=:phoneNo where id=:id")
    @Transactional
    @Modifying
    void updateDoctorDb(String phoneNo);
    //
    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto(a.id,a.age,a.speciality,a.gender,a.phoneNo,a.exp,a.degree) from DoctorDetails a where id=:id")
    DoctorFormDto getDoctorById(Long id);
    //
    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto(dd.id,ld.name,ld.emailId,ld.profilePic,dd.speciality,dd.exp,dd.degree) from DoctorDetails dd inner join LoginDetails ld on  dd.loginId=ld.id and dd.id!=:id")
    List<DoctorListDto> getAllDoctors(Long id);


    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorDropdownDto(dd.id,ld.name,ld.emailId,dd.speciality) from DoctorDetails dd inner join LoginDetails ld on  dd.loginId=ld.id")
    List<DoctorDropdownDto> getDoctorDetails();

    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto(dd.id,ld.name,ld.emailId,ld.profilePic,dd.speciality,dd.exp,dd.degree) from DoctorDetails dd inner join LoginDetails ld on  dd.loginId=ld.id and speciality=:speciality")
    List<DoctorListDto> getAllDoctorsBySpeciality(String speciality);

    //
    @Query(value = "select id from doctor_details d where d.login_id=:id", nativeQuery = true)
    Long isIdAvailable(Long id);

    @Query(value = "select distinct(speciality) from doctor_details d where d.speciality=:speciality", nativeQuery = true)
    String isSpecialityAvailable(String speciality);

    //
    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto(ld.name,ld.emailId,dd.speciality,dd.phoneNo,dd.gender,dd.age,dd.degree,dd.exp) from DoctorDetails dd inner join LoginDetails ld on dd.id=ld.id and dd.id=:id")
    DoctorBasicDetailsDto findDoctorById(Long id);

    @Query(value = "insert into doctor_details (id,age,gender,login_id,phone_no,speciality,experience,degree) values(:doctorId,:age,:gender,:loginId,:phoneNo,:speciality,:exp,:degree)",nativeQuery = true)
    @Transactional
    @Modifying
    void insertARowIntoTheTable(Long doctorId,Short age,String speciality,String phoneNo,String gender,Long loginId,short exp,String degree);


    @Query(value = "select p.gender from patients p join appointments a where a.patient_id = p.id and doctor_id=:doctorId group by p.id",nativeQuery = true)
    List<String> genderChart(Long doctorId);

    @Query(value = "select p.blood_group from patients p join appointments a where a.patient_id = p.id and doctor_id=:doctorId group by p.id",nativeQuery = true)
    List<String> bloodGroupChart(Long doctorId);

    @Query(value = "select p.age from patients p join appointments a where a.patient_id = p.id and doctor_id=:doctorId group by p.id",nativeQuery = true)
    List<Long> ageGroupChart(Long doctorId);




}
