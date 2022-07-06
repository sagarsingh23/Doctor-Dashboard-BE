//package com.dashboard.doctor_dashboard.repository;
//
//import com.dashboard.doctor_dashboard.entities.model.Attributes;
//import com.dashboard.doctor_dashboard.entities.model.Patient;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@DataJpaTest
//class PatientRepositoryTest {
//
//    @MockBean
//    private PatientRepository patientRepository;
//
//    @BeforeEach
//    void init(){
//        MockitoAnnotations.openMocks(this);
//        System.out.println("setting up");
//    }
//
//    @AfterEach
//    void tearDown() {
//        System.out.println("tearing down..");
//        patientRepository.deleteAll();
//    }
//
//
//    @Test
//    void getAllPatientByDoctorId() {
//        final Long doctorId = 1L;
//        ArrayList<Patient> patientList = new ArrayList<>();
//
//        Patient patient = new Patient();
//        patient.setAge(21);
//        patient.setCategory("orthology");
//        patient.setEmailId("sagarssn23@gmail.com");
//        patient.setFullName("Sagar Singh Negi");
//        patient.setMobileNo("900011112");
//        patient.setPID(1L);
//        patient.setGender("male");
//        patient.setLastVisitedDate(null);
//        patient.setStatus("Active");
//        patient.setAttributes(null);
//        patient.setDoctorDetails(null);
//
//        Patient newpatient = new Patient();
//        newpatient.setAge(21);
//        newpatient.setCategory("orthology");
//        newpatient.setEmailId("sagarssn23@gmail.com");
//        newpatient.setFullName("Sagar Singh Negi");
//        newpatient.setMobileNo("900011112");
//        newpatient.setPID(1L);
//        newpatient.setGender("male");
//        newpatient.setLastVisitedDate(null);
//        newpatient.setStatus("Active");
//        newpatient.setAttributes(null);
//        newpatient.setDoctorDetails(null);
//
//        patientList.addAll(Arrays.asList(patient,newpatient));
//
//        Mockito.when(patientRepository.getAllPatientByDoctorId(doctorId)).thenReturn(patientList);
//
//        assertThat(patientRepository.getAllPatientByDoctorId(doctorId)).isNotNull();
//        assertThat(patientRepository.getAllPatientByDoctorId(doctorId)).isEqualTo(patientList);
//        assertEquals(patientRepository.getAllPatientByDoctorId(doctorId).size(),patientList.size());
//
//
//    }
//
//    @Test
//    void getPatientByIdAndDoctorId() {
//        final Long id = 1L;
//        final Long doctorId = 1L;
//
//        Attributes attribute = new Attributes();
//        attribute.setBloodGroup("B+");
//        attribute.setBloodPressure(120L);
//        attribute.setBodyTemp(90D);
//        attribute.setSymptoms("fever,cough");
//        attribute.setAID(1L);
//        attribute.setGlucoseLevel(95L);
//
//        Patient patient = new Patient();
//        patient.setAge(21);
//        patient.setCategory("orthology");
//        patient.setEmailId("sagarssn23@gmail.com");
//        patient.setFullName("Sagar Singh Negi");
//        patient.setMobileNo("900011112");
//        patient.setPID(id);
//        patient.setGender("male");
//        patient.setLastVisitedDate(null);
//        patient.setStatus("Active");
//        patient.setAttributes(attribute);
//        patient.setDoctorDetails(null);
//
//
//        Mockito.when(patientRepository.getPatientByIdAndDoctorId(id,doctorId)).thenReturn(patient);
//
//        assertThat(patientRepository.getPatientByIdAndDoctorId(id,doctorId)).isNotNull();
//        assertThat(patientRepository.getPatientByIdAndDoctorId(id,doctorId)).isEqualTo(patient);
//    }
//
//    @Test
//    void changePatientStatus() {
//        final Long patientId = 1L;
//        String status = "Active";
//
//        patientRepository.changePatientStatus(patientId,status);
//        patientRepository.changePatientStatus(patientId,status);
//
//        verify(patientRepository,times(2)).changePatientStatus(patientId,status);
//    }
//
//    @Test
//    void getId() {
//        Long patientId = 1L;
//
//        Mockito.when(patientRepository.getId(patientId)).thenReturn(patientId);
//
//        assertThat(patientRepository.getId(patientId)).isNotNull();
//        assertEquals(patientRepository.getId(patientId),patientId);
//
//    }
//
//    @Test
//    void recentlyAddedPatient() {
//        final Long doctorId = 1L;
//        ArrayList<Patient> patientList = new ArrayList<>();
//
//        Patient patient = new Patient();
//        patient.setAge(21);
//        patient.setCategory("orthology");
//        patient.setEmailId("sagarssn23@gmail.com");
//        patient.setFullName("Sagar Singh Negi");
//        patient.setMobileNo("900011112");
//        patient.setPID(1L);
//        patient.setGender("male");
//        patient.setLastVisitedDate(null);
//        patient.setStatus("Active");
//        patient.setAttributes(null);
//        patient.setDoctorDetails(null);
//
//        Patient newpatient = new Patient();
//        newpatient.setAge(21);
//        newpatient.setCategory("orthology");
//        newpatient.setEmailId("sagarssn23@gmail.com");
//        newpatient.setFullName("Sagar Singh Negi");
//        newpatient.setMobileNo("900011112");
//        newpatient.setPID(1L);
//        newpatient.setGender("male");
//        newpatient.setLastVisitedDate(null);
//        newpatient.setStatus("Active");
//        newpatient.setAttributes(null);
//        newpatient.setDoctorDetails(null);
//
//        patientList.addAll(Arrays.asList(patient,newpatient));
//
//        Mockito.when(patientRepository.recentlyAddedPatient(doctorId)).thenReturn(patientList);
//
//        assertThat(patientRepository.recentlyAddedPatient(doctorId)).isNotNull();
//        assertThat(patientRepository.recentlyAddedPatient(doctorId)).isEqualTo(patientList);
//        assertEquals(patientRepository.recentlyAddedPatient(doctorId).size(),patientList.size());
//
//    }
//
//
//    @Test
//    void totalNoOfPatient() {
//        final Long doctorId =1L;
//        int count = 10;
//
//        Mockito.when(patientRepository.totalNoOfPatient(doctorId)).thenReturn(count);
//
//
//        assertEquals(patientRepository.totalNoOfPatient(doctorId),count);
//
//    }
//
//    @Test
//    void totalNoOfPatientAddedThisWeek() {
//        final Long doctorId =1L;
//        int count = 10;
//
//        Mockito.when(patientRepository.totalNoOfPatientAddedThisWeek(doctorId)).thenReturn(count);
//
//        assertEquals(patientRepository.totalNoOfPatientAddedThisWeek(doctorId),count);
//
//    }
//
//
//    @Test
//    void patientCategory() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String category1 = "orthology";
//        String category2 = "general";
//        list.addAll(Arrays.asList(category1,category2));
//
//        Mockito.when(patientRepository.patientCategory(doctorId)).thenReturn(list);
//
//        assertThat(patientRepository.patientCategory(doctorId)).isNotNull();
//        assertEquals(patientRepository.patientCategory(doctorId).size(),list.size());
//        assertEquals(patientRepository.patientCategory(doctorId),list);
//    }
//
//    @Test
//    void gender() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String gender1 = "Male";
//        String gender2 = "Female";
//        list.addAll(Arrays.asList(gender1,gender2));
//
//        Mockito.when(patientRepository.gender(doctorId)).thenReturn(list);
//
//        assertThat(patientRepository.gender(doctorId)).isNotNull();
//        assertEquals(patientRepository.gender(doctorId).size(),list.size());
//        assertEquals(patientRepository.gender(doctorId),list);
//
//    }
//
//    @Test
//    void activePatient() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String string1 = "Active";
//        String string2 = "Inactive";
//        list.addAll(Arrays.asList(string1,string2));
//
//        Mockito.when(patientRepository.activePatient(doctorId)).thenReturn(list);
//
//        assertThat(patientRepository.activePatient(doctorId)).isNotNull();
//        assertEquals(patientRepository.activePatient(doctorId).size(),list.size());
//        assertEquals(patientRepository.activePatient(doctorId),list);
//
//    }
//
//    @Test
//    void bloodGroup() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String bloodGroup1 = "A+";
//        String bloodGroup2 = "B+";
//        list.addAll(Arrays.asList(bloodGroup1,bloodGroup2));
//
//        Mockito.when(patientRepository.bloodGroup(doctorId)).thenReturn(list);
//
//        assertThat(patientRepository.bloodGroup(doctorId)).isNotNull();
//        assertEquals(patientRepository.bloodGroup(doctorId).size(),list.size());
//        assertEquals(patientRepository.bloodGroup(doctorId),list);
//
//    }
//
//    @Test
//    void ageChart() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String ageChart1 = "15-25";
//        String ageChart2 = "26-64";
//        list.addAll(Arrays.asList(ageChart1,ageChart2));
//
//        Mockito.when(patientRepository.ageChart(doctorId)).thenReturn(list);
//
//        assertThat(patientRepository.ageChart(doctorId)).isNotNull();
//        assertEquals(patientRepository.ageChart(doctorId).size(),list.size());
//        assertEquals(patientRepository.ageChart(doctorId),list);
//
//    }
//
//
//    @Test
//    void findDoctorNameByPatientId() {
//        final Long id = 1L;
//        String docName = "Sagar";
//
//        Mockito.when(patientRepository.findDoctorNameByPatientId(id)).thenReturn(docName);
//
//        assertThat(patientRepository.findDoctorNameByPatientId(id)).isNotNull();
//        assertEquals(patientRepository.findDoctorNameByPatientId(id),docName);
//
//
//    }
//
//    @Test
//    void findPatientNameByPatientId() {
//        final Long id = 1L;
//        String patientName = "Pranay";
//
//        Mockito.when(patientRepository.findPatientNameByPatientId(id)).thenReturn(patientName);
//        assertThat(patientRepository.findPatientNameByPatientId(id)).isNotNull();
//        assertEquals(patientRepository.findPatientNameByPatientId(id),patientName);
//
//    }
//
//    @Test
//    void referPatients() {
//        final Long id = 1L;
//        final Long doctorId = 1L;
//
//        String docName = "Sagar";
//        String patientName = "Pranay";
//
//        patientRepository.referPatients(doctorId,id,docName,patientName);
//        patientRepository.referPatients(doctorId,id,docName,patientName);
//
//        verify(patientRepository,times(2)).referPatients(doctorId,id,docName,patientName);
//
//    }
//
//    @Test
//    void getMessageForReferredPatient() {
//        final Long doctorId = 1L;
//        ArrayList<String> list = new ArrayList<>();
//
//        String message1 = "Patient1 referred";
//        String message2 = "Patient2 referred";
//        list.addAll(Arrays.asList(message1,message2));
//
//        Mockito.when(patientRepository.getMessageForReferredPatient(doctorId)).thenReturn(list);
//
//        assertThat(patientRepository.getMessageForReferredPatient(doctorId)).isNotNull();
//        assertEquals(patientRepository.getMessageForReferredPatient(doctorId).size(),list.size());
//        assertEquals(patientRepository.getMessageForReferredPatient(doctorId),list);
//    }
//
//    @Test
//    void changeStatus() {
//        final Long doctorId = 1L;
//
//        patientRepository.changeStatus(doctorId);
//        patientRepository.changeStatus(doctorId);
//        patientRepository.changeStatus(doctorId);
//
//        verify(patientRepository,times(3)).changeStatus(doctorId);
//
//    }
//}