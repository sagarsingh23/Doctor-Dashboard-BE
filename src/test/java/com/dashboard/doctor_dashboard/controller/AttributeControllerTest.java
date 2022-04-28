package com.dashboard.doctor_dashboard.controller;

import com.dashboard.doctor_dashboard.entity.dtos.NotesDto;
import com.dashboard.doctor_dashboard.service.patient_service.AttributeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class AttributeControllerTest {

    @Mock
    private AttributeService attributeService;

    @InjectMocks
    private AttributeController attributeController;

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
    void changePatientStatus() {
        NotesDto notesDto = new NotesDto();
        notesDto.setNotes("Note1");

        String value = "Notes updated!!!";
        Mockito.when(attributeService.changeNotes(Mockito.any(Long.class),
                Mockito.any(String.class))).thenReturn(value);

        String newNote = attributeController.changeNotes(1L,notesDto);

        assertEquals(value,newNote);

    }
}