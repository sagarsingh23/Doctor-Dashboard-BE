package com.dashboard.doctor_dashboard.entity.dtos;


import lombok.Getter;

@Getter
public class NotesDto {
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
