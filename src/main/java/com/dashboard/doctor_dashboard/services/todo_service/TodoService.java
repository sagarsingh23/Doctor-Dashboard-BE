package com.dashboard.doctor_dashboard.services.todo_service;

import com.dashboard.doctor_dashboard.entities.Todolist;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TodoService {

    ResponseEntity<GenericMessage> addTodo(Todolist todolist);

    ResponseEntity<GenericMessage> getTodoById(Long id);

    ResponseEntity<GenericMessage> getAllTodoByDoctorId(Long doctorId);

    ResponseEntity<GenericMessage> updateTodo(Long id, Todolist todolist);

    ResponseEntity<GenericMessage> deleteTodoById(Long id);
}
