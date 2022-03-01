package com.dashboard.doctor_dashboard.Repository.doctor_repository;

import com.dashboard.doctor_dashboard.Entity.doctor_entity.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DoctorRepository extends JpaRepository<DoctorDetails,Long> {
    public List<DoctorDetails> findByName(String name);
    public List<DoctorDetails>  findByAge(short age);
    public DoctorDetails findByEmail(String email);
    public List<DoctorDetails>  findBySpeciality(String speciality);
}
