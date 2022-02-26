package com.dashboard.doctor_dashboard.Repository.patient_repository;

import com.dashboard.doctor_dashboard.Entity.patient_entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {
}
