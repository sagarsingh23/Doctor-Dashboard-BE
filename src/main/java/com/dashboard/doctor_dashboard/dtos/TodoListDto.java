package com.dashboard.doctor_dashboard.dtos;

import com.dashboard.doctor_dashboard.entities.DoctorDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TodoListDto {
    @NotNull
    @NotEmpty(message = "description can't be empty")
    private String description;
    @NotNull(message = "status can't be null")
    private Boolean status;
    @NotNull(message = "doctorDetails can't be null")
    private DoctorDetails doctorDetails;
}
