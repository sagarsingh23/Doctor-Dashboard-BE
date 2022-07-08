package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.todo_service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/todolist")
@CrossOrigin(origins = "http://localhost:3000")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping()
    public ResponseEntity<GenericMessage> addTodo(@RequestBody TodoListDto todolist) {
        return todoService.addTodo(todolist);
    }

    @GetMapping("doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllTodoByDoctorId(@PathVariable("doctorId") Long doctorId) {
        return todoService.getAllTodoByDoctorId(doctorId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericMessage> getTodoById(@PathVariable("id") Long id) {
        return todoService.getTodoById(id);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteTodo(@PathVariable("id") Long id) {
        return todoService.deleteTodoById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericMessage> updateTodo(@PathVariable("id") Long id, @RequestBody TodoListDto todolist) {
        return todoService.updateTodo(id, todolist);
    }

}
