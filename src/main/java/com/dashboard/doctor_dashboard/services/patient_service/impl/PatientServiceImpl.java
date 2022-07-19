package com.dashboard.doctor_dashboard.services.patient_service.impl;

import com.dashboard.doctor_dashboard.entities.model.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.*;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import com.dashboard.doctor_dashboard.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private  PatientRepository patientRepository;
    private  AttributeRepository attributeRepository;
    private  DoctorRepository doctorRepository;
    private  AppointmentRepository appointmentRepository;
    private  PrescriptionRepository prescriptionRepository;
    private  LoginRepo loginRepo;
    private  ModelMapper mapper;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, AttributeRepository attributeRepository, DoctorRepository doctorRepository, AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository, LoginRepo loginRepo, ModelMapper mapper) {
        this.patientRepository = patientRepository;
        this.attributeRepository = attributeRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.loginRepo = loginRepo;
        this.mapper = mapper;
    }


    /**
     * This function of service is for adding patient details.
     * @param patient
     * @param loginId
     * @return ResponseEntity<GenericMessage> with status code 201.
     */
    @Override
    public ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient, Long loginId) {

        var genericMessage = new GenericMessage();

        Long temp = loginRepo.isIdAvailable(loginId);
        if(temp != null){
            patientRepository.
                    insertIntoPatient(patient.getAge(),patient.getMobileNo(),patient.getAlternateMobileNo(),
                            patient.getGender(), patient.getAddress(), patient.getBloodGroup(),loginId);

            log.debug("Patient Service::Patient Successfully Added..");

            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.CREATED) ;
        }else {
            throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND);
        }
    }


    /**
     * This function of service is for getting patient details by login id
     * @param loginId
     * @return ResponseEntity<GenericMessage> with status code 200 and patient details
     */
    @Override
    public ResponseEntity<GenericMessage> getPatientDetailsById(Long loginId) {
        var genericMessage = new GenericMessage();

        if(loginRepo.isIdAvailable(loginId) != null){
            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }else {
            throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND);
        }

    }


    /**
     * This function of service is for deleting patient by id
     * @param id
     * @return ResponseEntity<GenericMessage> with status code 204 and message successfully deleted.
     */
    @Override
    public ResponseEntity<GenericMessage> deletePatientById(Long id) {
        patientRepository.deleteById(id);
        log.debug("Patient Service:: Patient Deleted Successfully");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successfully deleted"),HttpStatus.NO_CONTENT);
    }


    /**
     * This function of service converts patient entity to patientEntityDto
     * @param patient
     * @return patientEntityDto which contains mobileNo,gender,age etc
     */
    // convert entity to dto
    private PatientEntityDto mapToDto(Patient patient) {
        return mapper.map(patient, PatientEntityDto.class);
    }


    /**
     * This function of service is for updating patient details
     * @param id
     * @param patient
     * @return ResponseEntity<GenericMessage> with status code 200 and message successfully updated.
     */
    @Override
    public ResponseEntity<GenericMessage> updatePatientDetails(Long id, PatientDetailsUpdateDto patient) {

        var genericMessage = new GenericMessage();
        if (loginRepo.existsById(patient.getId()) && patientRepository.getId(patient.getId())!=null) {
            patientRepository.updateMobileNo(patient.getMobileNo(),patient.getId());
            log.debug("Patient Service:: Patient Updated Successfully..");
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
        }
    }

    /**
     * This function of service is for getting all notification by patient id
     * @param patientId
     * @return ResponseEntity<GenericMessage> with status code 200 and list of doctorName and appointId.
     */
    @Override
    public ResponseEntity<GenericMessage> getNotifications(long patientId) {
        if (loginRepo.existsById(patientId) && patientRepository.getId(patientId)!=null) {
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.getNotifications(patientRepository.getId(patientId))), HttpStatus.OK);
        }
        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
    }

    /**
     * This function of service is for getting appointment details by appointmentId and patientId
     * @param appointmentId
     * @param patientId
     * @return ResponseEntity<GenericMessage> with status code 200 and appointment details
     */
    @Override
    public ResponseEntity<GenericMessage> viewAppointment(Long appointmentId,long patientId){


        if(patientRepository.getId(patientId)!=null){
            if(appointmentRepository.findById(appointmentId).isPresent() && appointmentRepository.getDoctorId(appointmentId) != null ){

                Long doctorId = doctorRepository.isIdAvailable(appointmentRepository.getDoctorId(appointmentId));
                if(doctorId != null) {
                    AppointmentViewDto viewDto =appointmentRepository.getBasicAppointmentDetails(appointmentId,patientRepository.getId(patientId));
                    viewDto.setEmail(appointmentRepository.getEmailById(doctorId));
                    viewDto.setAttributes(attributeRepository.getAttribute(appointmentId));
                    viewDto.setPrescription(prescriptionRepository.getAllPrescriptionByAppointment(appointmentId));

                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, viewDto), HttpStatus.OK);
                }
                else{
                    throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);}
            }
            else{
                throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);}
        }
        else{
            throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);}
    }

}
