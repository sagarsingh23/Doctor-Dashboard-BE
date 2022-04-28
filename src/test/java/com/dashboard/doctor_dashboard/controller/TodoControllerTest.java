package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.Todolist;
import com.dashboard.doctor_dashboard.service.todo_service.TodoService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
        Todolist todolist = new Todolist(1L,"hello",true,null);
        Mockito.when(todoService.addTodo(Mockito.any(Todolist.class))).thenReturn(todolist);
        Todolist newTodo = todoController.addTodo(todolist);
        assertEquals(todolist.getDescription(),newTodo.getDescription());
    }

    @Test
    void getAllTodoByDoctorId() {
        List<Todolist> list = new ArrayList<Todolist>();
        Todolist todolist1 = new Todolist(1L,"task1",true,null);
        Todolist todolist2 = new Todolist(2L,"task2",true,null);

        list.addAll(Arrays.asList(todolist1,todolist2));

        Mockito.when(todoService.getAllTodoByDoctorId(Mockito.any(Long.class))).thenReturn(list);
        List<Todolist> newList = todoController.getAllTodoByDoctorId(1L);

        assertEquals(list.size(),newList.size());
        assertEquals(todolist1.getDescription(),newList.get(0).getDescription());
        assertEquals(todolist2.getDescription(),newList.get(1).getDescription());

    }

    @Test
    void getTodoById() {
        Todolist todolist = new Todolist(1L,"hello",true,null);
        Mockito.when(todoService.getTodoById(Mockito.any(Long.class))).thenReturn(todolist);
        Todolist newTodo = todoController.getTodoById(1L);
        assertEquals(todolist.getDescription(),newTodo.getDescription());
    }

    @Test
    void deleteTodo() {
       Todolist todolist = new Todolist(1L,"hello",true,null);

       Mockito.when(todoService.deleteTodoById(Mockito.any(Long.class))).thenReturn("Deleted");

       String newString = todoController.deleteTodo(1L);
       assertEquals("Deleted",newString);
    }

    @Test
    void updateTodo() {
        Todolist todolist = new Todolist(1L,"hello",true,null);

        Mockito.when(todoService.updateTodo(Mockito.any(Long.class),Mockito.any(Todolist.class))).thenReturn(todolist);

        Todolist newTodoList = todoController.updateTodo(1L,todolist);

        assertEquals(todolist.getId(),newTodoList.getId());
        assertEquals(todolist.getDescription(),newTodoList.getDescription());

    }
}