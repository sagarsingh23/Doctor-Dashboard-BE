package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.AppointmentListDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<AppointmentListDto> getAllAppointmentByPatientId(Long patientId) {
        List<Appointment> appointments = appointmentRepository.getAllAppointmentByPatientId(patientId);
        List<AppointmentListDto> list = appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<AppointmentListDto> getAllAppointmentByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.getAllAppointmentByDoctorId(doctorId);
        List<AppointmentListDto> list = appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());

        return list;
    }

    @Override
    public Appointment getAppointmentById(Long appointId) {
        return appointmentRepository.getAppointmentById(appointId);
    }

    private AppointmentListDto mapToDto(Appointment appointment) {
        return mapper.map(appointment, AppointmentListDto.class);
    }

}
