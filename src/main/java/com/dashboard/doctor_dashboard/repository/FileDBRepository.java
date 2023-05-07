package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.FileDB;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDBRepository extends PagingAndSortingRepository<FileDB, Long> {
    FileDB findByAppointmentId(Long id);
}