package com.dashboard.doctor_dashboard.services.todo_service;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.model.Todolist;
import com.dashboard.doctor_dashboard.Utils.Constants;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.repository.TodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public ResponseEntity<GenericMessage> addTodo(TodoListDto todolist) {
        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.save(mapper.map(todolist,Todolist.class)));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<GenericMessage> getTodoById(Long id) {
        var genericMessage = new GenericMessage();

        Optional<Todolist> value = todoRepository.findById(id);
        if (value.isPresent()) {
            genericMessage.setData(value.get());
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);
        }
        return null;
    }

    @Override
    public ResponseEntity<GenericMessage> getAllTodoByDoctorId(Long doctorId) {
        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.findByDoctorId(doctorId));
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> updateTodo(Long id, TodoListDto todolist) {
        var genericMessage = new GenericMessage();

        Optional<Todolist> value1 = todoRepository.findById(id);
        if (value1.isPresent()) {
            Todolist value = value1.get();
            value.setDescription(todolist.getDescription());
            value.setStatus(todolist.getStatus());
            genericMessage.setData(todoRepository.save(value));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);
        }
        return null;
    }

    @Override
    public ResponseEntity<GenericMessage> deleteTodoById(Long id) {
        var genericMessage = new GenericMessage();

        todoRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        return new ResponseEntity<>(genericMessage, HttpStatus.OK);

    }
}
