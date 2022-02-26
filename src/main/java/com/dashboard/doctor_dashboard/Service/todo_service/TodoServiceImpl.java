package com.dashboard.doctor_dashboard.Service.todo_service;

import com.dashboard.doctor_dashboard.Entity.todo_entity.Todolist;
import com.dashboard.doctor_dashboard.Repository.todo_repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService{

    @Autowired
    private TodoRepository todoRepository;
    @Override
    public Todolist addlist(Todolist todolist) {
        return todoRepository.save(todolist);
    }

    @Override
    public Todolist getlistById(Long id) {
        return todoRepository.findById(id).get();
    }

    @Override
    public List<Todolist> getalllist() {
        return todoRepository.findAll();
    }

    @Override
    public Todolist updatelist(Long id, Todolist todolist) {
        Todolist value = todoRepository.findById(id).get();
        value.setDescription(todolist.getDescription());
        value.setStatus(todolist.getStatus());
        return todoRepository.save(value);
    }

    @Override
    public void deletelistById(Long id) {
        todoRepository.deleteById(id);

    }
}
