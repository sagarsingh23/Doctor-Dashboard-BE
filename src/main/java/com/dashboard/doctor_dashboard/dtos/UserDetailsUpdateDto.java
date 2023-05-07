package com.dashboard.doctor_dashboard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsUpdateDto {
  @NotNull
  private Long id;

  @Pattern(regexp = "^([0-9]{10})", message = "Number should be of 10 digits")
  private String mobileNo;

}
