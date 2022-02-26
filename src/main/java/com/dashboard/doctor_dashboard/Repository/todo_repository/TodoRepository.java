package com.dashboard.doctor_dashboard.Repository.todo_repository;

import com.dashboard.doctor_dashboard.Entity.todo_entity.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todolist,Long> {

}
