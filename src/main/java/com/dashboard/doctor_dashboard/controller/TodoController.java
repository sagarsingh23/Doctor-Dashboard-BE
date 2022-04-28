package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.Todolist;
import com.dashboard.doctor_dashboard.service.todo_service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/todolist")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping()
    public Todolist addTodo(@RequestBody Todolist todolist) {
        return todoService.addTodo(todolist);
    }

    @GetMapping("doctor/{doctorId}")
    public List<Todolist> getAllTodoByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return todoService.getAllTodoByDoctorId(doctorId);
    }

    @GetMapping("/{id}")
    public Todolist getTodoById(@PathVariable("id") Long id) {
        return todoService.getTodoById(id);

    }

    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable("id") Long id) {
        return todoService.deleteTodoById(id);

    }

    @PutMapping("/{id}")
    public Todolist updateTodo(@PathVariable("id") Long id, @RequestBody Todolist todolist) {
        return todoService.updateTodo(id, todolist);
    }

}
