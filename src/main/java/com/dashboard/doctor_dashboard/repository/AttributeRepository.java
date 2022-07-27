package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Attributes;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AttributeRepository extends PagingAndSortingRepository<Attributes, Long> {
    @Query(value = "update vitals set prescription =:prescription where appointment_id=:appointId ", nativeQuery = true)
    @Modifying
    @Transactional
    void changeNotes(Long appointId, String prescription);

    @Query(value = "select * from vitals where appointment_id=:appointId",nativeQuery = true)
    Attributes getAttribute(Long appointId);


    @Query(value = "select appointment_id from vitals where appointment_id=:appointId",nativeQuery = true)
    Long checkAppointmentPresent(Long appointId);

}
