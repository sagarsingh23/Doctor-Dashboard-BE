package com.dashboard.doctor_dashboard.controllers;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.model.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.model.Todolist;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.services.todo_service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TodoControllerTest {

    MockMvc mockMvc;

    @Mock
    private TodoService todoService;

    @Mock
    private ModelMapper mapper;


    @InjectMocks
    private TodoController todoController;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    void addTodo() throws Exception {
        DoctorDetails doctorDetails = new DoctorDetails();
        doctorDetails.setId(1L);
        TodoListDto todolist = new TodoListDto(1L,"hello",true,doctorDetails);

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,todolist);
        Mockito.when(todoService.addTodo(Mockito.any(TodoListDto.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.CREATED));
        String content = objectMapper.writeValueAsString(todolist);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/todolist").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isCreated());
    }

    @Test
    void getAllTodoByDoctorId() throws Exception {

        Todolist todolist1 = new Todolist(1L,"task1",true,null,null,null);
        Todolist todolist2 = new Todolist(2L,"task2",true,null,null,null);
        List<Todolist> list = new ArrayList<Todolist>(Arrays.asList(todolist1, todolist2));
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,list);

        Mockito.when(todoService.getAllTodoByDoctorId(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/todolist/doctor/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void getTodoById() throws Exception {
        Todolist todolist = new Todolist(1L,"hello",true,null,null,null);
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,todolist);

        Mockito.when(todoService.getTodoById(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1/todolist/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void deleteTodo() throws Exception {

        GenericMessage message  = new GenericMessage(Constants.SUCCESS,"Deleted");
        Mockito.when(todoService.deleteTodoById(Mockito.any(Long.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1/todolist/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void updateTodo() throws Exception {
        TodoListDto todolist = new TodoListDto(1L,"hello",true,null);
        GenericMessage message  = new GenericMessage(Constants.SUCCESS,todolist);
        String content = objectMapper.writeValueAsString(todolist);

        Mockito.when(todoService.updateTodo(Mockito.any(Long.class),Mockito.any(TodoListDto.class))).thenReturn(new ResponseEntity<>(message, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/todolist/1").contentType(MediaType.APPLICATION_JSON).content(content)).andExpect(status().isOk());

    }
}