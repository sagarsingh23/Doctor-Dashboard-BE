package com.dashboard.doctor_dashboard.Service;

import com.dashboard.doctor_dashboard.Controller.DoctorController;
import com.dashboard.doctor_dashboard.Entity.dtos.NotesDto;
import com.dashboard.doctor_dashboard.Repository.AttributeRepository;
import com.dashboard.doctor_dashboard.Repository.PatientRepository;
import com.dashboard.doctor_dashboard.Service.doctor_service.DoctorService;
import com.dashboard.doctor_dashboard.Service.patient_service.AttributeService;
import com.dashboard.doctor_dashboard.Service.patient_service.Impl.AttributeServiceImpl;
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
    void throwErrorWhenIdMisMatchForChangeNotes() {
        final Long id =1L;
        NotesDto notesDto = new NotesDto();
        notesDto.setNotes("Note1");

        Mockito.when(patientRepository.getId(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() ->{
            attributeService.changeNotes(id,notesDto.getNotes());
        });
    }
}