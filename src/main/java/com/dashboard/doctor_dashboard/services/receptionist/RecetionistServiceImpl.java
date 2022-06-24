package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecetionistServiceImpl implements ReceptionistService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    AttributeRepository attributeRepository;
    @Override
    public ResponseEntity<GenericMessage> getDoctorDetails() {
      return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, doctorRepository.getDoctorDetails()),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId) {
        List<Appointment> appointmentList = appointmentRepository.receptionistDoctorAppointment(doctorId);

        List<PatientViewDto> patientViewDto = appointmentList.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS , patientViewDto),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> updateAppointmentVitals(VitalsUpdateDto attributes, long appointmentId) {
        if(appointmentRepository.existsById(appointmentId)){
            attributeRepository.updateVitals(attributes.getBloodPressure(),attributes.getBodyTemperature(),attributes.getGlucoseLevels(),appointmentId);
            appointmentRepository.setStatus("Vitals updated",appointmentId);
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successful"), HttpStatus.OK);
        }

            throw new ResourceNotFoundException("appointments","id",appointmentId);

    }
    private PatientViewDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientViewDto.class);
    }

}
