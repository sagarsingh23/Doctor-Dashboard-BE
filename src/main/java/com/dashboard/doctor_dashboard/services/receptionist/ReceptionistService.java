package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReceptionistService {
    ResponseEntity<GenericMessage> getDoctorDetails();
    ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId,int pageNo);
    ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(int pageNo);

    ResponseEntity<GenericMessage> addAppointmentVitals(AttributesDto vitalsDto, Long appointmentId);

}
