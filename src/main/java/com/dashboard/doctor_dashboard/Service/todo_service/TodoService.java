package com.dashboard.doctor_dashboard.Service.todo_service;

import com.dashboard.doctor_dashboard.Entity.Todolist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TodoService {

    public Todolist addlist(Todolist todolist );
    public Todolist getlistById(Long id);
    public List<Todolist> getAllTodoByDoctorId(Long doctorId);
    public Todolist updatelist(Long id, Todolist todolist);
    public void deletelistById(Long id);
}
