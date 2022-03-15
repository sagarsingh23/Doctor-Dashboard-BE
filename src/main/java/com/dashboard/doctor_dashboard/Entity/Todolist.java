package com.dashboard.doctor_dashboard.Entity;

import com.dashboard.doctor_dashboard.Entity.DoctorDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "Todolist"
)

public class Todolist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private Boolean status;

    @OneToMany
    @JoinColumn(name = "d_id",nullable = false)
    private DoctorDetails doctorDetails;
}
