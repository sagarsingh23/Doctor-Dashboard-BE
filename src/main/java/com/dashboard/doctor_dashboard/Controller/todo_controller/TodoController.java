package com.dashboard.doctor_dashboard.Controller.todo_controller;

import com.dashboard.doctor_dashboard.Entity.todo_entity.Todolist;

import com.dashboard.doctor_dashboard.Service.todo_service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {
   @Autowired
    private TodoService todoService;

    @PostMapping("api/todolist")
    public Todolist addlist (@RequestBody Todolist todolist){
        return todoService.addlist(todolist);
    }

    @GetMapping("api/todolist")
    public List<Todolist> getalllist(){
        return todoService.getalllist();
    }

    @GetMapping("api/todolist/{id}")
    public Todolist getlistById(@PathVariable("id") Long id){
        return  todoService.getlistById(id);

    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id" ) Long id){
        todoService.deletelistById(id);
        return "successfully deleted";
    }
    @PutMapping("/{id}")
    public Todolist updatelist(@PathVariable("id") Long id,@RequestBody Todolist todolist){
        return todoService.updatelist(id,todolist);
    }



}
