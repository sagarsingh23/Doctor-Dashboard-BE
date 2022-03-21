package com.dashboard.doctor_dashboard.Repository;

import com.dashboard.doctor_dashboard.Entity.report.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB, Long> {
}