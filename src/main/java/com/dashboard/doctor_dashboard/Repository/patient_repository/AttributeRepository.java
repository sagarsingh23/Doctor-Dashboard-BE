package com.dashboard.doctor_dashboard.Repository.patient_repository;

import com.dashboard.doctor_dashboard.Entity.patient_entity.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attributes,Long> {
}
