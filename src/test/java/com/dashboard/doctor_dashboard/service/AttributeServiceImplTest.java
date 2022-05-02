package com.dashboard.doctor_dashboard.service;

import com.dashboard.doctor_dashboard.entity.dtos.NotesDto;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.service.patient_service.impl.AttributeServiceImpl;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AttributeServiceImplTest {

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private PatientRepository patientRepository;


    @InjectMocks
    private AttributeServiceImpl attributeService;


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
    void testChangeNotesIfIdPresent() {
        final Long id =1L;
        NotesDto notesDto = new NotesDto();
        notesDto.setNotes("Note1");

        Mockito.when(patientRepository.getId(id)).thenReturn(id);
        attributeService.changeNotes(1L,notesDto.getNotes());
        attributeService.changeNotes(1L,notesDto.getNotes());

        verify(attributeRepository,times(2)).changeNotes(id,notesDto.getNotes());

    }

    @Test
    void throwErrorWhenIdNotPresentForChangeNotes() {
        final Long id =1L;
        NotesDto notesDto = new NotesDto();
        notesDto.setNotes("Note1");

        Mockito.when(patientRepository.getId(id)).thenReturn(null);

        String notes=notesDto.getNotes();
        assertThrows(ResourceNotFoundException.class,() ->{

            attributeService.changeNotes(id,notes);
        });
    }

    @Test
    void throwErrorIfIdMisMatchForChangeNotes() {
        final Long id =1L;
        final Long newId = 2L;
        NotesDto notesDto = new NotesDto();
        notesDto.setNotes("Note1");

        Mockito.when(patientRepository.getId(id)).thenReturn(newId);
        String notes=notesDto.getNotes();
        assertThrows(ResourceNotFoundException.class,() ->{
            attributeService.changeNotes(id,notes);
        });
    }


}