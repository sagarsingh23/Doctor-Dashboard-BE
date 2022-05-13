package com.dashboard.doctor_dashboard.service.patient_service.impl;

import com.dashboard.doctor_dashboard.entity.Attributes;
import com.dashboard.doctor_dashboard.entity.Patient;
import com.dashboard.doctor_dashboard.entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entity.dtos.PatientListDto;
import com.dashboard.doctor_dashboard.exception.MyCustomException;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.service.patient_service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public static final String PATIENT = "Patient";



    @Autowired
    private ModelMapper mapper;


    @Override
    public Patient addPatient(Patient patient) {
         return patientRepository.save(patient);

    }

    @Override
    public List<PatientListDto> getAllPatientByDoctorId(Long doctorId) {
        List<Patient> patient = patientRepository.getAllPatientByDoctorId(doctorId);
        return patient.stream()
                .map(this::mapToDto2).collect(Collectors.toList());


    }

    @Override
    public PatientDto getPatientById(Long id, Long doctorId) throws MyCustomException {
        try {
            var patient = patientRepository.getPatientByIdAndDoctorId(id, doctorId);

            return mapToDto(patient);
        } catch (Exception e) {
            throw new MyCustomException(PATIENT, "id", id);
        }

    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Optional<Patient> patients = patientRepository.findById(id);
        Optional<Attributes> attributes = attributeRepository.findById(id);

        if (patients.isPresent() && attributes.isPresent()) {

            var value = patients.get();
            var value1 = attributes.get();

            value.setFullName(patient.getFullName());
            value.setAge(patient.getAge());
            value.setCategory(patient.getCategory());
            value.setEmailId(patient.getEmailId());
            value.setGender(patient.getGender());
            value.setLastVisitedDate(patient.getLastVisitedDate());
            value.setMobileNo(patient.getMobileNo());

            value1.setBloodGroup(patient.getAttributes().getBloodGroup());
            value1.setBloodPressure(patient.getAttributes().getBloodPressure());
            value1.setBodyTemp(patient.getAttributes().getBodyTemp());
            value1.setSymptoms(patient.getAttributes().getSymptoms());
            value1.setGlucoseLevel(patient.getAttributes().getGlucoseLevel());

            patientRepository.save(value);
            attributeRepository.save(value1);
            return value;
        } else {
            throw new ResourceNotFoundException(PATIENT, "id", id);
        }
    }

    @Override
    public void deletePatientById(Long id) {
        patientRepository.deleteById(id);
    }


    @Override
    public List<PatientListDto> recentlyAddedPatient(Long doctorId) {

        List<Patient> patient = patientRepository.recentlyAddedPatient(doctorId);
        return patient.stream()
                .map(this::mapToDto2).collect(Collectors.toList());


    }

    @Override
    public void changePatientStatus(Long id, String status) {
        if (patientRepository.getId(id) != null && patientRepository.getId(id).equals(id)) {
            patientRepository.changePatientStatus(id, status);
        } else {
            throw new ResourceNotFoundException(PATIENT, "id", id);
        }
    }


    //DashBoard Chart

    @Override
    public int totalNoOfPatient(Long doctorId) {
        return patientRepository.totalNoOfPatient(doctorId);
    }

    @Override
    public int totalNoOfPatientAddedThisWeek(Long doctorId) {
        return patientRepository.totalNoOfPatientAddedThisWeek(doctorId);
    }


    @Override
    public ArrayList<String> patientCategory(Long doctorId) {
        return patientRepository.patientCategory(doctorId);
    }

    @Override
    public ArrayList<String> gender(Long doctorId) {
        return patientRepository.gender(doctorId);
    }

    @Override
    public ArrayList<String> bloodGroup(Long doctorId) {
        return patientRepository.bloodGroup(doctorId);
    }

    @Override
    public ArrayList<String> ageChart(Long doctorId) {
        return patientRepository.ageChart(doctorId);
    }


    //Add-On feature Refer Patient


    @Override
    public String referPatients(Long doctorId, Long patientId) {

        String docName = patientRepository.findDoctorNameByPatientId(patientId);
        String patientName = patientRepository.findPatientNameByPatientId(patientId);

        patientRepository.referPatients(doctorId, patientId, docName, patientName);
        return "SuccessFull";
    }

    @Override
    public ArrayList<String> getMessageForReferredPatient(Long doctorId) {
        return patientRepository.getMessageForReferredPatient(doctorId);
    }

    @Override
    public void changeStatus(Long doctorId) {
        patientRepository.changeStatus(doctorId);
    }


    // convert entity to dto
    private PatientDto mapToDto(Patient patient) {
        return mapper.map(patient, PatientDto.class);
    }

    private PatientListDto mapToDto2(Patient patient) {
        return mapper.map(patient, PatientListDto.class);

    }


    @SuppressWarnings("squid:S3776")
    @Override
    public ArrayList<String> weeklyPatientCountChart(Long doctorId) {
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

        return newList;
    }
}
