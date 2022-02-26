package com.dashboard.doctor_dashboard.Entity.patient_entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "attributes"
)
public class Attributes {

    @Id
    @GeneratedValue(generator = "attribute_sequence")
    @SequenceGenerator(name = "attribute_sequence",initialValue = 1,allocationSize = 100)
    private Long aID;
    private Long bloodPressure;
    private String bloodGroup;
    private Long glucoseLevel;
    private Double bodyTemp;
    private String category;
    private Date lastVisitedDate;
    private String Notes;

    @JsonBackReference
    @OneToOne()
    @JoinColumn(name = "id")
    private Patient patient;



}
