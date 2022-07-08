package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.model.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todolist, Long> {

    @Query(value = "select * from todolist where doctor_id = :doctorId", nativeQuery = true)
    public List<Todolist> findByDoctorId(@Param(value = "doctorId") long doctorId);

}
