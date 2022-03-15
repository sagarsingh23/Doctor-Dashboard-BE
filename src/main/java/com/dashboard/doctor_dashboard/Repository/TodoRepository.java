package com.dashboard.doctor_dashboard.Repository;

import com.dashboard.doctor_dashboard.Entity.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todolist,Long> {

}
