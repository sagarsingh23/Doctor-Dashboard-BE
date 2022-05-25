package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private LoginRepo loginRepo;


    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @Autowired
    private ModelMapper mapper;


    @Override
    public Appointment addAppointment(Appointment appointment, HttpServletRequest request) {

        Long loginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(loginId) != null) {
            if (patientRepository.getId(appointment.getPatient().getPID()) != null && doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId()) != null) {
                return appointmentRepository.save(appointment);
            }
        }
        throw new ResourceNotFoundException("Patient", "id", loginId);
    }

    @Override
    public List<PatientAppointmentListDto> getAllAppointmentByPatientId(Long patientId) {
        List<Appointment> appointments = appointmentRepository.getAllAppointmentByPatientId(patientId);
        List<PatientAppointmentListDto> list = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<DoctorAppointmentListDto> getAllAppointmentByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.getAllAppointmentByDoctorId(doctorId);
        List<DoctorAppointmentListDto> list = appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());

        return list;
    }

    @Override
    public PatientProfileDto getAppointmentById(Long appointId) {
        Appointment appointment = appointmentRepository.getAppointmentById(appointId);
        return mapper.map(appointment,PatientProfileDto.class);
    }


    @Override
    public ResponseEntity<GenericMessage> recentAppointment(Long doctorId) {

        List<Appointment> appointments = appointmentRepository.recentAppointment(doctorId);
        List<DoctorAppointmentListDto> list =  appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list),HttpStatus.OK);
    }



    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentRepository.totalNoOfAppointment(doctorId)),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentRepository.totalNoOfAppointmentAddedThisWeek(doctorId)),HttpStatus.OK);
    }






    private DoctorAppointmentListDto mapToDto(Appointment appointment) {
        return mapper.map(appointment, DoctorAppointmentListDto.class);
    }

    private PatientAppointmentListDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientAppointmentListDto.class);
    }

}
