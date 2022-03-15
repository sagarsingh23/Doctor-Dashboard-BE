package com.dashboard.doctor_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private String bloodGroup;
    private Long glucoseLevel;
    private Double bodyTemp;
    private String Notes;
    private String symptoms;

    @JsonBackReference
    @OneToOne()
    @JoinColumn(name = "id")
    private Patient patient;



}
