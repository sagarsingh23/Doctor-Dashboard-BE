package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.web.bind.annotation.GetMapping;

public interface ReceptionistService {
    GenericMessage getDoctorDetails();
    GenericMessage getDoctorAppointments(Long doctorId);
    void updateAppointmentVitals();

}
