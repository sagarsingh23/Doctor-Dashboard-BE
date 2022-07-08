package com.dashboard.doctor_dashboard.services.receptionist;

import com.dashboard.doctor_dashboard.entities.dtos.AttributesDto;
import com.dashboard.doctor_dashboard.entities.model.Appointment;
import com.dashboard.doctor_dashboard.entities.model.Attributes;
import com.dashboard.doctor_dashboard.Utils.Constants;
import com.dashboard.doctor_dashboard.Utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.entities.dtos.PatientViewDto;
import com.dashboard.doctor_dashboard.exceptions.APIException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Override
    public ResponseEntity<GenericMessage> getDoctorDetails() {
      return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, doctorRepository.getDoctorDetails()),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> getDoctorAppointments(Long doctorId,int pageNo) {
        Pageable paging = PageRequest.of(pageNo, 10);
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            List<Appointment> appointmentList = appointmentRepository.receptionistDoctorAppointment(doctorId,paging).toList();

            List<PatientViewDto> patientViewDto = appointmentList.stream()
                    .map(this::mapToDto2).collect(Collectors.toList());
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, patientViewDto), HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }


    @Override
    public ResponseEntity<GenericMessage> todayAllAppointmentForClinicStaff(int pageNo) {
        List<Appointment> appointments = new ArrayList<>();
        Pageable paging = PageRequest.of(pageNo, 10);
        List<Appointment> appointmentList1 = appointmentRepository.todayAllAppointmentForClinicStaff1(paging).toList();
        List<Appointment> appointmentList2 = appointmentRepository.todayAllAppointmentForClinicStaff2(paging).toList();
        appointments.addAll(appointmentList1);
        appointments.addAll(appointmentList2);

        List<PatientViewDto> patientViewDto = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS , patientViewDto),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> addAppointmentVitals(AttributesDto vitalsDto, Long appointmentId) {
        if(appointmentRepository.existsById(appointmentId)){
            if(attributeRepository.checkAppointmentPresent(appointmentId) == null){
                appointmentRepository.setStatus("Vitals updated",appointmentId);
                attributeRepository.save(mapper.map(vitalsDto,Attributes.class));
               return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successful"), HttpStatus.CREATED);
             }
            else
                throw new APIException("update not allowed in this API endpoint.");

        }
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);

    }

    private PatientViewDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientViewDto.class);
    }

}
