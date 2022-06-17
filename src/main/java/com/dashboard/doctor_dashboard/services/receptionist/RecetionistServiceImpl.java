package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.Constants;
import com.dashboard.doctor_dashboard.entities.dtos.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientAppointmentListDto;
import com.dashboard.doctor_dashboard.entities.dtos.PatientViewDto;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class RecetionistServiceImpl implements ReceptionistService {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AppointmentRepository appointmentRepository;
    @Override
    public GenericMessage getDoctorDetails() {
      return new GenericMessage(Constants.SUCCESS, doctorRepository.getDoctorDetails());

    }

    @Override
    public GenericMessage getDoctorAppointments(Long doctorId) {
        List<Appointment> appointmentList = appointmentRepository.receptionistDoctorAppointment(doctorId);

        List<PatientViewDto> patientViewDto = appointmentList.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        return new GenericMessage(Constants.SUCCESS , patientViewDto);
    }

    @Override
    public void updateAppointmentVitals() {

    }
    private PatientViewDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientViewDto.class);
    }

}
