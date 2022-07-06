//package com.dashboard.doctor_dashboard.repository;
//
//import com.dashboard.doctor_dashboard.entities.dtos.NotesDto;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
////@ContextConfiguration({"classpath*:spring/applicationContext.xml"})
//@DataJpaTest
//class AttributeRepositoryTest {
//
//    @MockBean
//    private AttributeRepository attributeRepository;
//
//    @BeforeEach
//    void init(){
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @AfterEach
//    void tearDown() {
//        attributeRepository.deleteAll();
//    }
//
//
//    @Test
//    void testChangeNotes() {
//        final Long id = 1L;
//        NotesDto notesDto = new NotesDto();
//        notesDto.setNotes("Note1");
//
//        attributeRepository.changeNotes(id,notesDto.getNotes());
//        attributeRepository.changeNotes(id,notesDto.getNotes());
//
//        verify(attributeRepository,times(2)).changeNotes(id,notesDto.getNotes());
//
//    }
//}