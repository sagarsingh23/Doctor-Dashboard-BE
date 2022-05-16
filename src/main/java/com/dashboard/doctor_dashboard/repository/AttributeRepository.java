package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Attributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AttributeRepository extends JpaRepository<Attributes, Long> {
    @Query(value = "update attributes set notes =:notes where id=:patientId ", nativeQuery = true)
    @Modifying
    @Transactional
    void changeNotes(Long patientId, String notes);

}
