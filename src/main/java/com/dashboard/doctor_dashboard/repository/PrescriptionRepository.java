package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,Long> {

    @Query(value = "select * from prescriptions where appointment_id = :appointId",nativeQuery = true)
    List<Prescription> getAllPrescriptionByAppointment(Long appointId);
}
