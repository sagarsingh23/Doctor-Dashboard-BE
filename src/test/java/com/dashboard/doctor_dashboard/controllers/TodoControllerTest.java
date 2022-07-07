package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.model.Todolist;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.todo_service.TodoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



class TodoControllerTest {

    @Mock
    private TodoService todoService;


    @InjectMocks
    private TodoController todoController;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void addTodo() {
        TodoListDto todolist = new TodoListDto(1L,"hello",true,null);
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,todolist);
        Mockito.when(todoService.addTodo(Mockito.any(TodoListDto.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));
        ResponseEntity<GenericMessage> newTodo = todoController.addTodo(todolist);
        assertEquals(message.getData(),newTodo.getBody().getData());
    }

    @Test
    void getAllTodoByDoctorId() {
        List<Todolist> list = new ArrayList<Todolist>();
        Todolist todolist1 = new Todolist(1L,"task1",true,null,null,null);
        Todolist todolist2 = new Todolist(2L,"task2",true,null,null,null);
        list.addAll(Arrays.asList(todolist1,todolist2));
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);

        Mockito.when(todoService.getAllTodoByDoctorId(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));
        ResponseEntity<GenericMessage> newList = todoController.getAllTodoByDoctorId(1L);

        System.out.println(newList.getBody().getData());
        System.out.println(list);

        assertEquals(list,newList.getBody().getData());
    }

    @Test
    void getTodoById() {
        Todolist todolist = new Todolist(1L,"hello",true,null,null,null);
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,todolist);

        Mockito.when(todoService.getTodoById(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));
        ResponseEntity<GenericMessage> newTodo = todoController.getTodoById(1L);
        assertEquals(todolist,newTodo.getBody().getData());
    }

    @Test
    void deleteTodo() {
        Todolist todolist = new Todolist(1L,"hello",true,null,null,null);

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,"Deleted");

        Mockito.when(todoService.deleteTodoById(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));

        ResponseEntity<GenericMessage> newString = todoController.deleteTodo(1L);
        assertEquals("Deleted",newString.getBody().getData());
    }

    @Test
    void updateTodo() {
        TodoListDto todolist = new TodoListDto(1L,"hello",true,null);
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,todolist);

        Mockito.when(todoService.updateTodo(Mockito.any(Long.class),Mockito.any(TodoListDto.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));

        ResponseEntity<GenericMessage> newTodoList = todoController.updateTodo(1L,todolist);


        assertEquals(todolist,newTodoList.getBody().getData());

    }
}