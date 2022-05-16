package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.report.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, Long> {
    FileDB findByPatientId(Long id);
}