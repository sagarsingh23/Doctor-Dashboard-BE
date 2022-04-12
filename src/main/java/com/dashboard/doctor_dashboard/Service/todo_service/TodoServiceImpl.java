package com.dashboard.doctor_dashboard.Service.todo_service;

import com.dashboard.doctor_dashboard.Entity.Todolist;
import com.dashboard.doctor_dashboard.Repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoRepository todoRepository;
    @Override
    public Todolist addTodo(Todolist todolist) {
        return todoRepository.save(todolist);
    }

    @Override
    public Todolist getTodoById(Long id) {
        return todoRepository.findById(id).get();
    }

    @Override
    public List<Todolist> getAllTodoByDoctorId(Long doctorId) {
        return todoRepository.findByDoctorId(doctorId);
    }

    @Override
    public Todolist updateTodo(Long id, Todolist todolist) {
        Todolist value = todoRepository.findById(id).get();
        value.setDescription(todolist.getDescription());
        value.setStatus(todolist.getStatus());
        return todoRepository.save(value);
    }

    @Override
    public String deleteTodoById(Long id) {
        todoRepository.deleteById(id);
        return"successfully deleted";
    }
}
