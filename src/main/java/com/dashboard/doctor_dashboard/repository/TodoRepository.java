package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Todolist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends PagingAndSortingRepository<Todolist, Long> {

    @Query(value = "select * from todolist where doctor_id = :doctorId", nativeQuery = true)
    List<Todolist> findByDoctorId(@Param(value = "doctorId") long doctorId);

}
