package com.dashboard.doctor_dashboard.services.patient_service.impl;

import com.dashboard.doctor_dashboard.entities.Attributes;
import com.dashboard.doctor_dashboard.entities.Patient;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.MyCustomException;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.*;
import com.dashboard.doctor_dashboard.services.patient_service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private LoginRepo loginRepo;



    public static final String PATIENT = "Patient";


    @Autowired
    private ModelMapper mapper;


    @Override
    public ResponseEntity<GenericMessage> addPatient(PatientEntityDto patient,Long loginId) {

        GenericMessage genericMessage = new GenericMessage();

        Long temp = loginRepo.isIdAvailable(loginId);
        if(temp != null){
            patientRepository.
                    insertIntoPatient(patient.getAge(),patient.getMobileNo(),patient.getAlternateMobileNo(),
                            patient.getGender(), patient.getAddress(), patient.getBloodGroup(),loginId);
            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }else {
            throw new ResourceNotFoundException("Login Details", "id", loginId);
        }
    }

    @Override
    public ResponseEntity<GenericMessage> getAllPatientByDoctorId(Long doctorId) {
        List<Patient> patient = patientRepository.getAllPatientByDoctorId(doctorId);
        List<PatientListDto> list = patient.stream()
                .map(this::mapToDto2).collect(Collectors.toList());

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> getPatientById(Long id, Long doctorId) throws MyCustomException {
        try {
            var patient = patientRepository.getPatientByIdAndDoctorId(id, doctorId);

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,mapToDto(patient)),HttpStatus.OK);

        } catch (Exception e) {
            throw new MyCustomException(PATIENT, "id", id);
        }

    }

    @Override
    public ResponseEntity<GenericMessage> getPatientDetailsById(Long loginId) {
        GenericMessage genericMessage = new GenericMessage();

        if(loginRepo.isIdAvailable(loginId) != null){
            var patientDetails = patientRepository.getPatientByLoginId(loginId);
            genericMessage.setData(mapToDto(patientDetails));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK) ;
        }else {
            throw new ResourceNotFoundException("Login Details", "id", loginId);
        }

    }

    @Override
    public ResponseEntity<GenericMessage> updatePatient(Long id, Patient patient) {

//        Optional<Patient> patients = patientRepository.findByLoginId(id);
//        Optional<Patient> patients = patientRepository.getId(id);
        Optional<Attributes> attributes = attributeRepository.findById(id);

//        if (patients.isPresent() && attributes.isPresent()) {
//
//            var value = patients.get();
//            var value1 = attributes.get();

//            value.setFullName(patient.getFullName());
//            value.setAge(patient.getAge());
//            value.setCategory(patient.getCategory());
//            value.setEmailId(patient.getEmailId());
//            value.setGender(patient.getGender());
//            value.setLastVisitedDate(patient.getLastVisitedDate());
//            value.setMobileNo(patient.getMobileNo());
//
//            value1.setBloodGroup(patient.getAttributes().getBloodGroup());
//            value1.setBloodPressure(patient.getAttributes().getBloodPressure());
//            value1.setBodyTemp(patient.getAttributes().getBodyTemp());
//            value1.setSymptoms(patient.getAttributes().getSymptoms());
//            value1.setGlucoseLevel(patient.getAttributes().getGlucoseLevel());

//
        return null;

    }

    @Override
    public ResponseEntity<GenericMessage> deletePatientById(Long id) {
        patientRepository.deleteById(id);
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"successfully deleted"),HttpStatus.OK);
    }


    @Override
    public ResponseEntity<GenericMessage> recentlyAddedPatient(Long doctorId) {

        List<Patient> patient = patientRepository.recentlyAddedPatient(doctorId);
        List<PatientListDto> list =  patient.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> changePatientStatus(Long id, String status) {
        if (patientRepository.getId(id) != null && patientRepository.getId(id).equals(id)) {
            patientRepository.changePatientStatus(id, status);

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"Status Updated!!!"),HttpStatus.OK);

        } else {
            throw new ResourceNotFoundException(PATIENT, "id", id);
        }
    }


    //DashBoard Chart

    @Override
    public ResponseEntity<GenericMessage> totalNoOfPatient(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientRepository.totalNoOfPatient(doctorId)),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> totalNoOfPatientAddedThisWeek(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientRepository.totalNoOfPatientAddedThisWeek(doctorId)),HttpStatus.OK);
    }


    @Override
    public ResponseEntity<GenericMessage> patientCategory(Long doctorId) {

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientRepository.patientCategory(doctorId)),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericMessage> gender(Long doctorId) {

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientRepository.gender(doctorId)),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericMessage> bloodGroup(Long doctorId) {

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientRepository.bloodGroup(doctorId)),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericMessage> ageChart(Long doctorId) {

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientRepository.ageChart(doctorId)),HttpStatus.OK);

    }


    //Add-On feature Refer Patient


    @Override
    public ResponseEntity<GenericMessage> referPatients(Long doctorId, Long patientId) {

        String docName = patientRepository.findDoctorNameByPatientId(patientId);
        String patientName = patientRepository.findPatientNameByPatientId(patientId);

        patientRepository.referPatients(doctorId, patientId, docName, patientName);
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"Patient Referred SuccessFully"),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> getMessageForReferredPatient(Long doctorId) {

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,patientRepository.getMessageForReferredPatient(doctorId)),HttpStatus.OK);

    }

    @Override
    public ResponseEntity<GenericMessage> changeStatus(Long doctorId) {
        patientRepository.changeStatus(doctorId);
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"All Messages have been deleted!!!"),HttpStatus.OK);
    }


    // convert entity to dto
    private PatientEntityDto mapToDto(Patient patient) {
        return mapper.map(patient, PatientEntityDto.class);
    }

    private PatientListDto mapToDto2(Patient patient) {
        return mapper.map(patient, PatientListDto.class);

    }


    @SuppressWarnings("squid:S3776")
    @Override
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId) {
        int lengthOfMonth = LocalDate.now().lengthOfMonth();

        ArrayList<String> newList = new ArrayList<>();
        var year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        var month= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

        var firstWeek="1-7";
        var secondWeek="8-14";
        var thirdWeek="15-21";
        var fourthWeek="22-28";
        var lastWeek="29-"+lengthOfMonth;
        var firstWeekCount=0;
        var secondWeekCount=0;
        var thirdWeekCount=0;
        var fourthWeekCount=0;
        var lastWeekCount=0;


        ArrayList<Date> dateList = patientRepository.getAllDatesByDoctorId(doctorId);

        ArrayList<LocalDate> localDateList =new ArrayList<>();
        for (Date date:dateList) {
            localDateList.add(date.toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        for (var i=0;i<localDateList.size();i++)
        {
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"01")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"08")) || localDateList.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"01"))){
                 firstWeekCount++;

               }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"08")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"15")) || localDateList.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"08"))){
                secondWeekCount++;
               }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"15")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"22"))|| localDateList.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"15"))){
                thirdWeekCount++;
               }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"22")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"29"))|| localDateList.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"22"))){
                fourthWeekCount++;
               }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"29")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+lengthOfMonth)) || localDateList.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"29"))){
                lastWeekCount++;
               }
        }

        newList.add(firstWeek+","+firstWeekCount);
        newList.add(secondWeek+","+secondWeekCount);
        newList.add(thirdWeek+","+thirdWeekCount);
        newList.add(fourthWeek+","+fourthWeekCount);
        newList.add(lastWeek+","+lastWeekCount);

        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,newList),HttpStatus.OK);
    }


    @Override
    public ResponseEntity<GenericMessage> updatePatientDetails(Long id, PatientDetailsUpdateDto patient) {

        GenericMessage genericMessage = new GenericMessage();

//        Optional<Patient> patients = patientRepository.findById(id);
        if (loginRepo.existsById(patient.getPatientId()) && patientRepository.getId(patient.getPatientId())!=null) {
            Long patientId=patientRepository.getId(patient.getPatientId());
//            var value = patients.get();
////            value.setAddress(patient.getAddress());
////            value.setAlternateMobileNo(patient.getAlternateMobileNo());
////            value.setAge(patient.getAge());
////            value.setGender(patient.getGender());
////            value.setBloodGroup(patient.getBloodGroup());
//            value.setMobileNo(patient.getMobileNo());
            patientRepository.updateMobileNo(patient.getMobileNo(),patient.getPatientId());
//            patientRepository.save(value);

//            genericMessage.setData(mapper.map(value,PatientDetailsUpdateDto.class));
            genericMessage.setStatus(Constants.SUCCESS);
            return new ResponseEntity<>(genericMessage, HttpStatus.OK);

        } else {
            throw new ResourceNotFoundException(PATIENT, "id", id);
        }
    }

    @Override
    public ResponseEntity<GenericMessage> getNotifications(long patientId) {
        System.out.println("get Id"+patientRepository.getId(patientId));
//        System.out.println(loginRepo.existsById(patientId)&&patientRepository.getId(patientId));
        if (loginRepo.existsById(patientId)&&patientRepository.getId(patientId)!=null) {

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.getNotifications(patientRepository.getId(patientId))), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("patient","id",patientId);
    }
    @Override
    public ResponseEntity<GenericMessage> viewAppointment(Long appointmentId,long patientId){

        if(loginRepo.isIdAvailable(patientId)!=null&&patientRepository.getId(patientId)!=null){
            System.out.println(appointmentId);
            if(attributeRepository.findById(appointmentId)!=null){

                AppointmentViewDto viewDto =appointmentRepository.getBasicAppointmentDetails(appointmentId,patientRepository.getId(patientId));

                Long doctorId=doctorRepository.isIdAvailable(appointmentRepository.getDoctorId(appointmentId));
//                viewDto.setGender(appointmentRepository.getGenderById(doctorId));
                viewDto.setEmail(appointmentRepository.getEmailById(doctorId));
                viewDto.setAttributes(attributeRepository.getAttribute(appointmentId));
                viewDto.setPrescription(prescriptionRepository.getAllPrescriptionByAppointment(appointmentId));
                return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,viewDto),HttpStatus.OK);
            }
            else {
                throw new ResourceNotFoundException("appointment","id",appointmentId);
            }
        }else {
            throw new ResourceNotFoundException("patient","id",patientId);
        }
    }

}
