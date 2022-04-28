package com.dashboard.doctor_dashboard.service;

import com.dashboard.doctor_dashboard.entity.Attributes;
import com.dashboard.doctor_dashboard.entity.Patient;
import com.dashboard.doctor_dashboard.entity.dtos.PatientDto;
import com.dashboard.doctor_dashboard.entity.dtos.PatientListDto;
import com.dashboard.doctor_dashboard.entity.dtos.StatusDto;
import com.dashboard.doctor_dashboard.repository.AttributeRepository;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.service.patient_service.impl.PatientServiceImpl;
import com.dashboard.doctor_dashboard.exception.MyCustomException;
import com.dashboard.doctor_dashboard.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private PatientServiceImpl patientService;


    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        System.out.println("setting up");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearing down..");
    }


    @Test
    void addPatient() {
        Patient patient = new Patient();
        patient.onCreate();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(null);
        patient.setDoctorDetails(null);

        Mockito.doReturn(patient).when(patientRepository).save(Mockito.any(Patient.class));

        Patient newPatient = patientService.addPatient(patient);

        assertThat(newPatient).isNotNull();
        verify(patientRepository).save(Mockito.any(Patient.class));

    }

    @Test
    void getAllPatientByDoctorId() {
        final Long doctorId = 1L;
        ArrayList<Patient> patientList = new ArrayList<>();

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(null);
        patient.setDoctorDetails(null);

        Patient newpatient = new Patient();
        newpatient.setAge(21);
        newpatient.setCategory("orthology");
        newpatient.setEmailId("sagarssn23@gmail.com");
        newpatient.setFullName("Sagar Singh Negi");
        newpatient.setMobileNo("900011112");
        newpatient.setPID(1L);
        newpatient.setGender("male");
        newpatient.setLastVisitedDate(null);
        newpatient.setStatus("Active");
        newpatient.setAttributes(null);
        newpatient.setDoctorDetails(null);

        patientList.addAll(Arrays.asList(patient,newpatient));


        PatientListDto patient1 = new PatientListDto();
        patient1.setAge(21);
        patient1.setCategory("orthology");
        patient1.setEmailId("sagarssn23@gmail.com");
        patient1.setFullName("Sagar Singh Negi");
        patient1.setMobileNo("900011112");
        patient1.setPID(1L);
        patient1.setGender("male");
        patient1.setLastVisitedDate(null);
        patient1.setStatus("Active");

        Mockito.when(patientRepository.getAllPatientByDoctorId(doctorId)).thenReturn(patientList);
        Mockito.when(mapper.map(patientList,PatientListDto.class)).thenReturn(patient1);

        List<PatientListDto> newList = patientService.getAllPatientByDoctorId(doctorId);
        assertThat(newList).isNotNull();
        assertEquals(newList.size(),patientList.size());
    }

    @Test
    void checkIfIdAvailableForGetPatientById() {
        final Long id = 1L;
        final Long doctorId = 1L;

        PatientDto patient1 = new PatientDto();
        patient1.setAge(21);
        patient1.setCategory("orthology");
        patient1.setEmailId("sagarssn23@gmail.com");
        patient1.setFullName("Sagar Singh Negi");
        patient1.setMobileNo("900011112");
        patient1.setPID(id);
        patient1.setGender("male");
        patient1.setLastVisitedDate(null);
        patient1.setStatus("Active");
        patient1.setAttributes(null);


        Attributes attribute = new Attributes();
        attribute.setBloodGroup("B+");
        attribute.setBloodPressure(120L);
        attribute.setBodyTemp(90D);
        attribute.setSymptoms("fever,cough");
        attribute.setAID(1L);
        attribute.setGlucoseLevel(95L);

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(id);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(attribute);
        patient.setDoctorDetails(null);

        Mockito.when(patientRepository.getPatientByIdAndDoctorId(id,doctorId)).thenReturn(patient);
        Mockito.when(mapper.map(patient,PatientDto.class)).thenReturn(patient1);

        PatientDto newPatient = patientService.getPatientById(id,doctorId);

        assertThat(newPatient).isNotNull();
        assertEquals(newPatient.getPID(),patient.getPID());
        assertEquals(newPatient.getFullName(),patient.getFullName());
    }


    @Test
    void throwErrorIfIdNotAvailable(){

        final Long id = 1L;
        final Long doctorId = 1L;

        Attributes attribute = new Attributes();
        attribute.setBloodGroup("B+");
        attribute.setBloodPressure(120L);
        attribute.setBodyTemp(90D);
        attribute.setSymptoms("fever,cough");
        attribute.setAID(1L);
        attribute.setGlucoseLevel(95L);

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(id);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(attribute);
        patient.setDoctorDetails(null);

        Mockito.when(patientRepository.getPatientByIdAndDoctorId(id,doctorId))
                        .thenThrow(MyCustomException.class);

        assertThrows(MyCustomException.class,()->{
            patientService.getPatientById(id,doctorId);
        });

    }

    @Test
    void updatePatient() {
        final Long id = 1L;
        Attributes attribute = new Attributes();
        attribute.setBloodGroup("B+");
        attribute.setBloodPressure(120L);
        attribute.setBodyTemp(90D);
        attribute.setSymptoms("fever,cough");
        attribute.setAID(1L);
        attribute.setGlucoseLevel(95L);



        Patient patient = new Patient();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(id);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(attribute);
        patient.setDoctorDetails(null);

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        Mockito.when(attributeRepository.findById(id)).thenReturn(Optional.of(patient.getAttributes()));

        Patient newPatient = patientService.updatePatient(id,patient);
        assertThat(newPatient).isNotNull();
        assertEquals(newPatient.getPID(),patient.getPID());
        assertEquals(newPatient.getAttributes().getAID(),patient.getAttributes().getAID());

    }

    @Test
    void checkIfIdNotPresentInDBForUpdatePatient() {
        final Long id = 1L;
        Attributes attribute = new Attributes();
        attribute.setBloodGroup("B+");
        attribute.setBloodPressure(120L);
        attribute.setBodyTemp(90D);
        attribute.setSymptoms("fever,cough");
        attribute.setAID(1L);
        attribute.setGlucoseLevel(95L);



        Patient patient = new Patient();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(id);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(attribute);
        patient.setDoctorDetails(null);

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(attributeRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.updatePatient(id,patient);
        });


    }

    @Test
    void checkIfIdNotPresentInDBUpdatePatient() {
        final Long id = 1L;
        Attributes attribute = new Attributes();
        attribute.setBloodGroup("B+");
        attribute.setBloodPressure(120L);
        attribute.setBodyTemp(90D);
        attribute.setSymptoms("fever,cough");
        attribute.setAID(1L);
        attribute.setGlucoseLevel(95L);



        Patient patient = new Patient();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(id);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(attribute);
        patient.setDoctorDetails(null);

        Mockito.when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        Mockito.when(attributeRepository.findById(id)).thenReturn(Optional.empty());


        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.updatePatient(id,patient);
        });


    }


    @Test
    void deletePatientById() {
        Long id = 1L;

        patientService.deletePatientById(id);
        patientService.deletePatientById(id);

        verify(patientRepository,times(2)).deleteById(id);
    }

    @Test
    void recentlyAddedPatient() {
        final Long doctorId = 1L;
        ArrayList<Patient> patientList = new ArrayList<>();

        Patient patient = new Patient();
        patient.setAge(21);
        patient.setCategory("orthology");
        patient.setEmailId("sagarssn23@gmail.com");
        patient.setFullName("Sagar Singh Negi");
        patient.setMobileNo("900011112");
        patient.setPID(1L);
        patient.setGender("male");
        patient.setLastVisitedDate(null);
        patient.setStatus("Active");
        patient.setAttributes(null);
        patient.setDoctorDetails(null);

        Patient newpatient = new Patient();
        newpatient.setAge(21);
        newpatient.setCategory("orthology");
        newpatient.setEmailId("sagarssn23@gmail.com");
        newpatient.setFullName("Sagar Singh Negi");
        newpatient.setMobileNo("900011112");
        newpatient.setPID(1L);
        newpatient.setGender("male");
        newpatient.setLastVisitedDate(null);
        newpatient.setStatus("Active");
        newpatient.setAttributes(null);
        newpatient.setDoctorDetails(null);

        patientList.addAll(Arrays.asList(patient,newpatient));


        PatientListDto patient1 = new PatientListDto();
        patient1.setAge(21);
        patient1.setCategory("orthology");
        patient1.setEmailId("sagarssn23@gmail.com");
        patient1.setFullName("Sagar Singh Negi");
        patient1.setMobileNo("900011112");
        patient1.setPID(1L);
        patient1.setGender("male");
        patient1.setLastVisitedDate(null);
        patient1.setStatus("Active");

        Mockito.when(patientRepository.recentlyAddedPatient(doctorId)).thenReturn(patientList);
        Mockito.when(mapper.map(patientList,PatientListDto.class)).thenReturn(patient1);

        List<PatientListDto> newList = patientService.recentlyAddedPatient(doctorId);
        assertThat(newList).isNotNull();
        assertEquals(newList.size(),patientList.size());

    }

    @Test
    void changePatientStatus() {
        Long id = 1L;
        StatusDto statusDto = new StatusDto();
        statusDto.setStatus("Active");

        Mockito.when(patientRepository.getId(id)).thenReturn(id);
        patientService.changePatientStatus(id,statusDto.getStatus());
        patientService.changePatientStatus(id,statusDto.getStatus());

        verify(patientRepository,times(2)).changePatientStatus(id,statusDto.getStatus());
    }
    @Test
    void throwErrorIfIdNotPresentInDbForStatus() {
        Long id = 1L;
        StatusDto statusDto = new StatusDto();
        statusDto.setStatus("Active");

        Mockito.when(patientRepository.getId(id)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.changePatientStatus(id,statusDto.getStatus());
        });
    }
    @Test
    void throwErrorIfIdMisMatchForStatus() {
        Long id = 1L;
        Long newId = 5L;
        StatusDto statusDto = new StatusDto();
        statusDto.setStatus("Active");

        Mockito.when(patientRepository.getId(id)).thenReturn(newId);

        assertThrows(ResourceNotFoundException.class,() -> {
            patientService.changePatientStatus(id,statusDto.getStatus());
        });
    }


    @Test
    void totalNoOfPatient() {
        final Long doctorId =1L;
        int count = 10;

        Mockito.when(patientRepository.totalNoOfPatient(doctorId)).thenReturn(count);

        int newCount = patientService.totalNoOfPatient(doctorId);

        assertEquals(newCount,count);

    }

    @Test
    void totalNoOfActivePatient() {
        final Long doctorId =1L;
        int count = 10;

        Mockito.when(patientRepository.totalNoOfActivePatient(doctorId)).thenReturn(count);

        int newCount = patientService.totalNoOfActivePatient(doctorId);


        assertEquals(newCount,count);

    }


    @Test
    void patientCategory() {
        final Long doctorId = 1L;
        ArrayList<String> list = new ArrayList<>();

        String category1 = "orthology";
        String category2 = "general";
        list.addAll(Arrays.asList(category1,category2));

        Mockito.when(patientRepository.patientCategory(doctorId)).thenReturn(list);

        ArrayList<String> newList = patientService.patientCategory(doctorId);

        assertThat(newList).isNotNull();
        assertEquals(newList.size(),list.size());
        assertEquals(newList,list);
    }

    @Test
    void gender() {
        final Long doctorId = 1L;
        ArrayList<String> list = new ArrayList<>();

        String gender1 = "Male";
        String gender2 = "Female";
        list.addAll(Arrays.asList(gender1,gender2));

        Mockito.when(patientRepository.gender(doctorId)).thenReturn(list);

        ArrayList<String> newList = patientService.gender(doctorId);

        assertThat(newList).isNotNull();
        assertEquals(newList.size(),list.size());
        assertEquals(newList,list);
    }

    @Test
    void activePatient() {
        final Long doctorId = 1L;
        ArrayList<String> list = new ArrayList<>();

        String string1 = "Active";
        String string2 = "Inactive";
        list.addAll(Arrays.asList(string1,string2));

        Mockito.when(patientRepository.activePatient(doctorId)).thenReturn(list);

        ArrayList<String> newList = patientService.activePatient(doctorId);

        assertThat(newList).isNotNull();
        assertEquals(newList.size(),list.size());
        assertEquals(newList,list);
    }

    @Test
    void bloodGroup() {
        final Long doctorId = 1L;
        ArrayList<String> list = new ArrayList<>();

        String bloodGroup1 = "A+";
        String bloodGroup2 = "B+";
        list.addAll(Arrays.asList(bloodGroup1,bloodGroup2));

        Mockito.when(patientRepository.bloodGroup(doctorId)).thenReturn(list);

        ArrayList<String> newList = patientService.bloodGroup(doctorId);

        assertThat(newList).isNotNull();
        assertEquals(newList.size(),list.size());
        assertEquals(newList,list);
    }

    @Test
    void ageChart() {
        final Long doctorId = 1L;
        ArrayList<String> list = new ArrayList<>();

        String ageChart1 = "15-25";
        String ageChart2 = "26-64";
        list.addAll(Arrays.asList(ageChart1,ageChart2));

        Mockito.when(patientRepository.ageChart(doctorId)).thenReturn(list);

        ArrayList<String> newList = patientService.ageChart(doctorId);

        assertThat(newList).isNotNull();
        assertEquals(newList.size(),list.size());
        assertEquals(newList,list);
    }

    @Test
    void referPatients() {
        final Long id = 1L;
        final Long doctorId = 1L;

        String docName = "Sagar";
        String patientName = "Pranay";

        Mockito.when(patientRepository.findDoctorNameByPatientId(id)).thenReturn(docName);
        Mockito.when(patientRepository.findPatientNameByPatientId(id)).thenReturn(patientName);

        patientService.referPatients(doctorId,id);
        patientService.referPatients(doctorId,id);

        verify(patientRepository,times(2)).referPatients(doctorId,id,docName,patientName);
    }

    @Test
    void getMessageForReferredPatient() {
        final Long doctorId = 1L;
        ArrayList<String> list = new ArrayList<>();

        String message1 = "Patient1 referred";
        String message2 = "Patient2 referred";
        list.addAll(Arrays.asList(message1,message2));

        Mockito.when(patientRepository.getMessageForReferredPatient(doctorId)).thenReturn(list);

        ArrayList<String> newList = patientService.getMessageForReferredPatient(doctorId);

        assertThat(newList).isNotNull();
        assertEquals(newList.size(),list.size());
        assertEquals(newList,list);
    }

    @Test
    void changeStatus() {
        final Long doctorId = 1L;

        patientService.changeStatus(doctorId);
        patientService.changeStatus(doctorId);
        patientService.changeStatus(doctorId);

        verify(patientRepository,times(3)).changeStatus(doctorId);

    }
}