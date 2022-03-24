package com.dashboard.doctor_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "attributes"
)
public class Attributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aID;
    private Long bloodPressure;

    @NotEmpty
    @Pattern(regexp = "O-|O+|A-|B-|A+|AB-|B+|AB+",flags = Pattern.Flag.CASE_INSENSITIVE)
    private String bloodGroup;

    private Long glucoseLevel;
    private Double bodyTemp;
    private String Notes;

    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "^[\\p{L} .'-]+$")
    private String symptoms;


    @JsonBackReference
    @OneToOne()
    @JoinColumn(name = "id")
    private Patient patient;


}
