package com.dashboard.doctor_dashboard.entities.dtos;

import com.dashboard.doctor_dashboard.entities.Attributes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDetailsUpdateDto {
  @NotNull
  private Long patientId;
  @NotNull
  @NotEmpty
  private String mobileNo;

}
