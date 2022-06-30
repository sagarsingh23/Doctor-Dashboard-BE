package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReceptionistService {
    ResponseEntity<GenericMessage> getDoctorDetails();
    ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId);
    ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff();

    ResponseEntity<GenericMessage> addAppointmentVitals(Attributes vitalsDto, Long appointmentId);

}
