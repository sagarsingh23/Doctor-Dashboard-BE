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
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.*;

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
        List<Patient> patients = patientRepository.getAllPatientByDoctorId(doctorId);


        return mapToDto2(patients);

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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            value.setFullName(patient.getFullName());
            value.setAge(patient.getAge());
            value.setCategory(patient.getCategory());
            value.setEmailId(patient.getEmailId());
            value.setGender(patient.getGender());
            value.setLastVisitedDate(LocalDate.parse(patient.getLastVisitedDate(),formatter));
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
        return mapToDto2(patient);

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
    public int totalNoOfActivePatient(Long doctorId) {
        return patientRepository.totalNoOfActivePatient(doctorId);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        PatientDto patientDto=new PatientDto(patient.getPID(),patient.getFullName(),patient.getEmailId(),patient.getStatus(),patient.getCategory(),LocalDate.parse(patient.getLastVisitedDate(),formatter),patient.getMobileNo(),patient.getGender(),patient.getAge(),patient.getAttributes());
        return patientDto;
//        return mapper.map(patient, PatientDto.class);
    }

    private List<PatientListDto> mapToDto2(List<Patient> patients) {
        List<PatientListDto> patientListDtos=new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Patient patient:patients) {
            patientListDtos.add(new PatientListDto(patient.getPID(),patient.getFullName(),patient.getEmailId(),patient.getStatus(),patient.getCategory(),LocalDate.parse(patient.getLastVisitedDate(),formatter),patient.getMobileNo(),patient.getGender(),patient.getAge()));
        }

        return patientListDtos;
    }


    @Override
    public ArrayList<String> activePatient(Long doctorId) {
        int length = LocalDate.now().lengthOfMonth();
        System.out.println(LocalDate.now().getDayOfMonth());
        ArrayList<String> newList = new ArrayList<>();
        String year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String month= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);

        String firstWeek="1-7";
        String secondWeek="8-14";
        String thirdWeek="15-21";
        String fourthWeek="22-28";
        String lastWeek="29-"+length;
        int firstWeekCount=0;
        int secondWeekCount=0;
        int thirdWeekCount=0;
        int fourthWeekCount=0;
        int lastWeekCount=0;


        ArrayList<Date> a = patientRepository.activeDate(doctorId);

        System.out.println(a.size());
        ArrayList<LocalDate> b =new ArrayList<>();
        for (Date date2:a) {
            b.add(date2.toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        System.out.println("dadte=> "+b);

        for (int i=0;i<b.size();i++)
        {
            if(b.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"01")) && b.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"08")) || b.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"01"))){
                 firstWeekCount++;
                 //System.out.println("ff= "+ firstWeekCount);
               }
            if(b.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"08")) && b.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"15")) || b.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"08"))){
                secondWeekCount++;
               }
            if(b.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"15")) && b.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"22"))|| b.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"15"))){
                thirdWeekCount++;
               }
            if(b.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"22")) && b.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+"29"))|| b.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"22"))){
                fourthWeekCount++;
               }
            if(b.get(i).isAfter(LocalDate.parse(year+"-0"+month+"-"+"29")) && b.get(i).isBefore(LocalDate.parse(year+"-0"+month+"-"+length)) || b.get(i).equals(LocalDate.parse(year+"-0"+month+"-"+"29"))){
                lastWeekCount++;
               }
        }

        newList.add(firstWeek+","+firstWeekCount);
        newList.add(secondWeek+","+secondWeekCount);
        newList.add(thirdWeek+","+thirdWeekCount);
        newList.add(fourthWeek+","+fourthWeekCount);
        newList.add(lastWeek+","+lastWeekCount);

        System.out.println("weekly count= "+ newList);

        return newList;
    }
}
