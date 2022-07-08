package com.dashboard.doctor_dashboard.services;

import com.dashboard.doctor_dashboard.entities.dtos.TodoListDto;
import com.dashboard.doctor_dashboard.entities.model.Todolist;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.repository.TodoRepository;
import com.dashboard.doctor_dashboard.services.todo_service.TodoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private TodoServiceImpl todoService;




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
    void testAddList() {
        TodoListDto todolist = new TodoListDto(1L,"hello",true,null);
        Todolist todolist1 = new Todolist(1L,"hello",true,null,null,null);


        Mockito.when(mapper.map(todolist,Todolist.class)).thenReturn(todolist1);
        Mockito.doReturn(todolist1).when(todoRepository).save(Mockito.any(Todolist.class));

        ResponseEntity<GenericMessage> newTodo = todoService.addTodo(todolist);

        assertThat(todolist).isNotNull();
        verify(todoRepository).save(Mockito.any(Todolist.class));

    }


    @Test
    void testGetListById() {
        final Long id = 1L;
        Todolist todolist = new Todolist(id,"hello",true,null,null,null);

        Mockito.when(todoRepository.findById(id)).thenReturn(Optional.of(todolist));

        ResponseEntity<GenericMessage> newTodo = todoService.getTodoById(id);
        System.out.println(newTodo);

        assertThat(newTodo).isNotNull();
        assertEquals(todolist,newTodo.getBody().getData());
    }

    @Test
    void checkIfIdNotPresentInDBForGetListById() {
        final Long id = 1L;
        Todolist todolist = new Todolist(id,"hello",true,null,null,null);

        Mockito.when(todoRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<GenericMessage> newTodo = todoService.getTodoById(id);

        assertThat(newTodo).isNull();
    }

    @Test
    void getAllTodoByDoctorId() {

        final Long id = 1L;
        List<Todolist> list = new ArrayList<Todolist>();
        Todolist todolist1 = new Todolist(1L,"task1",true,null,null,null);
        Todolist todolist2 = new Todolist(2L,"task2",true,null,null,null);

        list.addAll(Arrays.asList(todolist1,todolist2));

        Mockito.when(todoRepository.findByDoctorId(id)).thenReturn(list);

        ResponseEntity<GenericMessage> newList = todoService.getAllTodoByDoctorId(id);

        assertThat(newList).isNotNull();
        assertEquals(list,newList.getBody().getData());
    }

    @Test
    void updateList() {
        final Long id = 1L;

        TodoListDto todolist = new TodoListDto(1L,"hello",true,null);
        Todolist todolist1 = new Todolist(1L,"hello",true,null,null,null);

        Mockito.when(todoRepository.findById(id)).thenReturn(Optional.of(todolist1));

        todoService.updateTodo(id,todolist);
        todoService.updateTodo(id,todolist);

        verify(todoRepository,times(2)).findById(id);

    }


    @Test
    void checkIfIdNotPresentInDBForUpdateList() {
        final Long id = 1L;
        TodoListDto todolist = new TodoListDto(1L,"hello",true,null);

        Mockito.when(todoRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<GenericMessage> newTodo = todoService.updateTodo(id,todolist);

        assertThat(newTodo).isNull();
    }

    @Test
    void deleteListById() {
        final Long id =1L;

        todoService.deleteTodoById(id);
        todoService.deleteTodoById(id);

        verify(todoRepository,times(2)).deleteById(id);
    }
}