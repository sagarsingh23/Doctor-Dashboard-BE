package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientViewDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFound;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceptionistServiceImpl implements ReceptionistService {
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
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            List<Appointment> appointmentList = appointmentRepository.receptionistDoctorAppointment(doctorId);

            List<PatientViewDto> patientViewDto = appointmentList.stream()
                    .map(this::mapToDto2).collect(Collectors.toList());
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, patientViewDto), HttpStatus.OK);
        }
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
    }


    @Override
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff() {
        List<Appointment> appointments = new ArrayList<>();
        List<Appointment> appointmentList1 = appointmentRepository.todayAllAppointmentForClinicStaff1();
        List<Appointment> appointmentList2 = appointmentRepository.todayAllAppointmentForClinicStaff2();
        appointments.addAll(appointmentList1);
        appointments.addAll(appointmentList2);

        List<PatientViewDto> patientViewDto = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS , patientViewDto),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> addAppointmentVitals(Attributes vitalsDto, Long appointmentId) {
        if(appointmentRepository.existsById(appointmentId)){
            if(attributeRepository.checkAppointmentPresent(appointmentId) == null){
                appointmentRepository.setStatus("Vitals updated",appointmentId);
                attributeRepository.save(vitalsDto);
               return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successful"), HttpStatus.OK);
             }
            else
                throw new APIException(HttpStatus.BAD_REQUEST,"update not allowed in this API endpoint.");

        }
        throw new ResourceNotFound(Constants.APPOINTMENT_NOT_FOUND);

    }

    private PatientViewDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientViewDto.class);
    }

}
