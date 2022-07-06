//package com.dashboard.doctor_dashboard.repository;
//
//import com.dashboard.doctor_dashboard.entities.model.Todolist;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class TodoRepositoryTest {
//
//    @MockBean
//    private TodoRepository todoRepository;
//
//    @BeforeEach
//    void init(){
//        MockitoAnnotations.openMocks(this);
//        System.out.println("setting up");
//    }
//
//    @AfterEach
//    void tearDown() {
//        System.out.println("tearing down..");
//        todoRepository.deleteAll();
//    }
//
//
//    @Test
//    void findByDoctorId() {
//        final Long doctorId = 1L;
//        List<Todolist> list = new ArrayList<Todolist>();
//        Todolist todolist1 = new Todolist(1L,"task1",true,null);
//        Todolist todolist2 = new Todolist(2L,"task2",true,null);
//
//        list.addAll(Arrays.asList(todolist1,todolist2));
//
//        Mockito.when(todoRepository.findByDoctorId(doctorId)).thenReturn(list);
//
//        assertThat(todoRepository.findByDoctorId(doctorId)).isNotNull();
//        assertEquals(todoRepository.findByDoctorId(doctorId).size(),list.size());
//        assertEquals(todoRepository.findByDoctorId(doctorId),list);
//
//
//    }
//}