package com.dashboard.doctor_dashboard.Service;

import com.dashboard.doctor_dashboard.Entity.Todolist;
import com.dashboard.doctor_dashboard.Repository.DoctorRepository;
import com.dashboard.doctor_dashboard.Repository.TodoRepository;
import com.dashboard.doctor_dashboard.Service.doctor_service.DoctorServiceImpl;
import com.dashboard.doctor_dashboard.Service.todo_service.TodoServiceImpl;
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
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TodoServiceImplTest {

    @Mock
    private TodoRepository todoRepository;

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
        Todolist todolist = new Todolist(1L,"hello",true,null);

        Mockito.doReturn(todolist).when(todoRepository).save(Mockito.any(Todolist.class));

        Todolist newTodo = todoService.addlist(todolist);

        assertThat(todolist).isNotNull();
        verify(todoRepository).save(Mockito.any(Todolist.class));

    }


    @Test
    void testGetListById() {
        final Long id = 1L;
        Todolist todolist = new Todolist(id,"hello",true,null);

        Mockito.when(todoRepository.findById(id)).thenReturn(Optional.of(todolist));

        Todolist newTodo = todoService.getlistById(id);
        System.out.println(newTodo);

        assertThat(newTodo).isNotNull();
        assertEquals(todolist.getDescription(),newTodo.getDescription());
    }

    @Test
    void getAllTodoByDoctorId() {

        final Long id = 1L;
        List<Todolist> list = new ArrayList<Todolist>();
        Todolist todolist1 = new Todolist(1L,"task1",true,null);
        Todolist todolist2 = new Todolist(2L,"task2",true,null);

        list.addAll(Arrays.asList(todolist1,todolist2));

        Mockito.when(todoRepository.findByDoctorId(id)).thenReturn(list);

        List<Todolist> newList = todoService.getAllTodoByDoctorId(id);

        assertEquals(list.size(),newList.size());
        assertEquals(todolist1.getDescription(),newList.get(0).getDescription());
        assertEquals(todolist2.getDescription(),newList.get(1).getDescription());
    }

    @Test
    void updatelist() {
        final Long id = 1L;
        Todolist todolist = new Todolist(1L,"hello",true,null);

        Mockito.when(todoRepository.findById(id)).thenReturn(Optional.of(todolist));
        todoService.updatelist(id,todolist);
        todoService.updatelist(id,todolist);

        verify(todoRepository,times(2)).findById(id);

    }

    @Test
    void deletelistById() {
        final Long id =1L;

        todoService.deletelistById(id);
        todoService.deletelistById(id);

        verify(todoRepository,times(2)).deleteById(id);
    }
}