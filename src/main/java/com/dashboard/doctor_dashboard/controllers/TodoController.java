package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.TodoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1/todolist")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class TodoController {


    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * @param todolist contains fields id,status and description
     * @return Successful message after creating todos for the doctor
     */
    @ApiOperation("Saves the todo in the todolist database")
    @PostMapping()
    public ResponseEntity<GenericMessage> addTodo(@Valid @RequestBody TodoListDto todolist) {
        log.info("TodoController::addTodo");

        return todoService.addTodo(todolist);
    }

    /**
     * @param doctorId is used as path variable
     * @return All todos for the doctor
     */
    @ApiOperation("Api for getting all todos by doctor id")
    @GetMapping("doctor/{doctorId}")
    public ResponseEntity<GenericMessage> getAllTodoByDoctorId(@PathVariable("doctorId") Long doctorId) {
        log.info("TodoController::getAllTodoByDoctorId");

        return todoService.getAllTodoByDoctorId(doctorId);
    }

    /**
     * @param id is used as path variable
     * @return task details on the basis of id provided
     */
    @ApiOperation("Show todo details on the basis of id provided")
    @GetMapping("/{id}")
    public ResponseEntity<GenericMessage> todoById(@PathVariable("id") Long id) {
        log.info("TodoController::getTodoById");

        return todoService.getTodoById(id);
    }

    /**
     * @param id is used as path variable
     * @return Successfully delete after triggering delete api.
     */
    @ApiOperation("delete the todos from the database")
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericMessage> deleteTodo(@PathVariable("id") Long id) {
        log.info("TodoController::deleteTodo");
        return todoService.deleteTodoById(id);
    }

    /**
     * @param id is used as path variable
     * @param todolist contains fields id,status and description
     * @return Successfully updated after updating task details  in database
     */
    @ApiOperation("updates the todos from the database")
    @PutMapping("/private/{id}")
    public ResponseEntity<GenericMessage> updateTodo(@PathVariable("id") Long id, @Valid @RequestBody TodoListDto todolist, HttpServletRequest request) {
        log.info("TodoController::updateTodo");
        return todoService.updateTodo(id, todolist);
    }
}
