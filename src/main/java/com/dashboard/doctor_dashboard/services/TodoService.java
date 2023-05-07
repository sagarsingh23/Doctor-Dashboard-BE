package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TodoService {

    ResponseEntity<GenericMessage> addTodo(TodoListDto todolist);

    ResponseEntity<GenericMessage> getTodoById(Long id);

    ResponseEntity<GenericMessage> getAllTodoByDoctorId(Long doctorId);

    ResponseEntity<GenericMessage> updateTodo(Long id, TodoListDto todolist);

    ResponseEntity<GenericMessage> deleteTodoById(Long id);
}
