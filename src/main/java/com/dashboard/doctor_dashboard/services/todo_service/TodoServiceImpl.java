package com.dashboard.doctor_dashboard.services.todo_service;

import com.dashboard.doctor_dashboard.entities.Todolist;
import com.dashboard.doctor_dashboard.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Todolist addTodo(Todolist todolist) {
        return todoRepository.save(todolist);
    }

    @Override
    public Todolist getTodoById(Long id) {
        Optional<Todolist> value = todoRepository.findById(id);
        if (value.isPresent()) {
            return value.get();
        }
        return null;
    }

    @Override
    public List<Todolist> getAllTodoByDoctorId(Long doctorId) {
        return todoRepository.findByDoctorId(doctorId);
    }

    @Override
    public Todolist updateTodo(Long id, Todolist todolist) {
        Optional<Todolist> value1 = todoRepository.findById(id);
        if (value1.isPresent()) {
            Todolist value = value1.get();
            value.setDescription(todolist.getDescription());
            value.setStatus(todolist.getStatus());
            return todoRepository.save(value);
        }
        return null;
    }

    @Override
    public String deleteTodoById(Long id) {
        todoRepository.deleteById(id);
        return "successfully deleted";
    }
}
