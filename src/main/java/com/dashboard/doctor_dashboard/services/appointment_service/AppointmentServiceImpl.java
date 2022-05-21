package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.AppointmentListDto;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.services.appointment_service.AppointmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    public Appointment addAppointment(Appointment appointment) {

        System.out.println("patent id"+appointment.getPatient().getPID());
        System.out.println("doctor id"+appointment.getDoctorDetails().getId());
        return appointmentRepository.save(appointment);
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
