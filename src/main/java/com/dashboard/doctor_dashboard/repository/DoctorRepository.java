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

    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorFormDto(a.id,a.age,a.speciality,a.gender,a.phoneNo) from DoctorDetails a where id=:id")
    DoctorFormDto getDoctorById(Long id);

    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorListDto(a.id,a.firstName,a.email,a.speciality) from DoctorDetails a where a.id!=:id")
    List<DoctorListDto> getAllDoctors(Long id);

    @Query(value = "select id from doctor_details d where d.id=:id", nativeQuery = true)
    Long isIdAvailable(Long id);

    @Query(value = "select new com.dashboard.doctor_dashboard.entities.dtos.DoctorBasicDetailsDto(a.firstName,a.email,a.speciality,a.phoneNo,a.gender,a.age) from DoctorDetails a where id=:id")
    DoctorBasicDetailsDto findDoctorById(Long id);
}
