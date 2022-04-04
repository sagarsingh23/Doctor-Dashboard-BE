package com.dashboard.doctor_dashboard.Controller;

import com.dashboard.doctor_dashboard.Entity.Todolist;

import com.dashboard.doctor_dashboard.Service.todo_service.TodoService;
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
    public Todolist addTodo(@RequestBody Todolist todolist){
        return todoService.addlist(todolist);
    }

    @GetMapping("doctor/{doctorId}")
    public List<Todolist> getAllTodoByDoctorId(@PathVariable("doctorId") Long doctorId){
        return todoService.getAllTodoByDoctorId(doctorId);
    }

    @GetMapping("/{id}")
    public Todolist getTodoById(@PathVariable("id") Long id){
        return todoService.getlistById(id);

    }
    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable("id" ) Long id){
        todoService.deletelistById(id);
        return "successfully deleted";
    }
    @PutMapping("/{id}")
    public Todolist updateTodo(@PathVariable("id") Long id,@RequestBody Todolist todolist){
        return todoService.updatelist(id,todolist);
    }

}
