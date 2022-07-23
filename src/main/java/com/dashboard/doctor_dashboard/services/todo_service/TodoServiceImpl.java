package com.dashboard.doctor_dashboard.services.todo_service;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.model.Todolist;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.TodoRepository;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * TodoServiceImpl
 */
@Service
@Slf4j
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private ModelMapper mapper;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, ModelMapper mapper) {
        this.todoRepository = todoRepository;
        this.mapper = mapper;
    }


    /**
     * This function of service is for adding todos/task  for doctor.
     * @param todolist which contains fields description,status and doctor details
     * @return ResponseEntity<GenericMessage> with status code 201.
     */
    @Override
    public ResponseEntity<GenericMessage> addTodo(TodoListDto todolist) {
        log.info("inside: TodoServiceImpl::addTodo");

        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.save(mapper.map(todolist,Todolist.class)));
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::addTodo");

        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }

    /**
     * This function of service is for getting task of the doctor by id
     * @param id
     * @return ResponseEntity<GenericMessage> with status code 200 and task.
     */
    @Override
    public ResponseEntity<GenericMessage> getTodoById(Long id) {
        log.info("inside: TodoServiceImpl::getTodoById");
        var genericMessage = new GenericMessage();

        Optional<Todolist> value = todoRepository.findById(id);
        if (value.isPresent()) {
            genericMessage.setData(value.get());
            genericMessage.setStatus(Constants.SUCCESS);
            log.info("exit: TodoServiceImpl::getTodoById");
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);
        }
        return null;
    }


    /**
     * This function of service is for getting all todos of the doctor by id
     * @param doctorId
     * @return ResponseEntity<GenericMessage> with status code 200 and list of todos.
     */
    @Override
    public ResponseEntity<GenericMessage> getAllTodoByDoctorId(Long doctorId) {
        log.info("inside: TodoServiceImpl::getAllTodoByDoctorId");

        var genericMessage = new GenericMessage();

        genericMessage.setData(todoRepository.findByDoctorId(doctorId));
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::getAllTodoByDoctorId");

        return new ResponseEntity<>(genericMessage, HttpStatus.OK);
    }

    /**
     * This function of service is for updating tasks
     * @param id
     * @param todolist which contains fields description,status and doctor details
     * @return ResponseEntity<GenericMessage> with status code 200 and message successfully updated.
     */
    @Override
    public ResponseEntity<GenericMessage> updateTodo(Long id, TodoListDto todolist) {
        log.info("inside: TodoServiceImpl::updateTodo");

        var genericMessage = new GenericMessage();

        Optional<Todolist> value1 = todoRepository.findById(id);
        if (value1.isPresent()) {
            Todolist value = value1.get();
            value.setDescription(todolist.getDescription());
            value.setStatus(todolist.getStatus());
            genericMessage.setData(todoRepository.save(value));
            genericMessage.setStatus(Constants.SUCCESS);
            log.info("exit: TodoServiceImpl::updateTodo");
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);
        }
        log.info("TodoServiceImpl::updateTodo"+Constants.TODO_NOT_FOUND);
        throw new  ResourceNotFoundException(Constants.TODO_NOT_FOUND);
    }

    /**
     * This function of service is for deleting todos/task by id
     * @param id
     * @return ResponseEntity<GenericMessage> with status code 204 and message successfully deleted.
     */
    @Override
    public ResponseEntity<GenericMessage> deleteTodoById(Long id) {
        log.info("inside: TodoServiceImpl::deleteTodoById");
        var genericMessage = new GenericMessage();

        todoRepository.deleteById(id);
        genericMessage.setData("successfully deleted");
        genericMessage.setStatus(Constants.SUCCESS);
        log.info("exit: TodoServiceImpl::deleteTodoById");
        return new ResponseEntity<>(genericMessage, HttpStatus.NO_CONTENT);

    }
}
