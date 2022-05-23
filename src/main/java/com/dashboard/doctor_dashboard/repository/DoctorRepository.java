package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto;
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


    @Query(value = "update DoctorDetails set age=:age,speciality=:speciality,gender=:gender,phoneNo=:phoneNo where id=:id")
    @Transactional
    @Modifying
    void updateDoctorDb(Short age, String speciality, String gender, String phoneNo, long id);
    //
    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto(a.id,a.age,a.speciality,a.gender,a.phoneNo) from DoctorDetails a where id=:id")
//@Query(value = "select com.dashboard.doctor_dashboard.entity.dtos.Doctora.id,a.age,a.speciality,a.gender,a.phoneNo from DoctorDetails a where id=:id")
    DoctorFormDto getDoctorById(Long id);
    //
    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto(dd.id,ld.name,ld.emailId,dd.speciality) from DoctorDetails dd inner join LoginDetails ld on  dd.loginId=ld.id and dd.id!=:id")
    List<DoctorListDto> getAllDoctors(Long id);

    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto(dd.id,ld.name,ld.emailId,dd.speciality) from DoctorDetails dd inner join LoginDetails ld on  dd.loginId=ld.id and speciality=:speciality")
    List<DoctorListDto> getAllDoctorsBySpeciality(String speciality);

    //
    @Query(value = "select id from doctor_details d where d.id=:id", nativeQuery = true)
    Long isIdAvailable(Long id);

    @Query(value = "select distinct(speciality) from doctor_details d where d.speciality=:speciality", nativeQuery = true)
    String isSpecialityAvailable(String speciality);

    //
    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto(ld.name,ld.emailId,dd.speciality,dd.phoneNo,dd.gender,dd.age) from DoctorDetails dd inner join LoginDetails ld on dd.id=ld.id and dd.id=:id")
    DoctorBasicDetailsDto findDoctorById(Long id);

    @Query(value = "insert into doctor_details (id,age,gender,login_id,phone_no,speciality) values(:doctorId,:age,:gender,:loginId,:phoneNo,:speciality)",nativeQuery = true)
    @Transactional
    @Modifying
    void insertARowIntoTheTable(Long doctorId,Short age,String speciality,String phoneNo,String gender,Long loginId);
}
