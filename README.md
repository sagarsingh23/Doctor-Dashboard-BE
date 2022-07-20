# Doctor-Dashboard-BE

Description
Technology is constantly changing the way we live our lives, from living in the metaverse to getting a prescription on a smartphone app, our lives are transforming fast. My Care an interactive Appointment booking system serves right from booking an appointment to tracking down the performance of the patient and manage the appointment flow. The flow of it helps the patient, doctor and a Patient care technician to easily track down the respective details of the Patient Appointment ,Patient details which include various vitals such as the Blood Group , Blood Pressure, Glucose level and the Temperature which is updated by the Patient care assistant and which can be reviewed by the doctor . The doctor would be able to assist the patient accordingly from the details updated.
The Patient will be able to book and reserve his appointment with the respective doctor for the diagnosed category within a week. The patient status right from the booking until the doctor completes and if its a follow is visible on the dashboard , there's section for the doctor where the previous prescription for the respective patient is visible. My Care includes all the necessary section to track the patient details right from booking the appointment to prescribing a medicine and assisting the patient.

# Technologies

### Project is created with:
```
Spring Boot
java 17
MySQL
Maven
JPA buddy
```

# Folder Structure of Backend
```
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── dashboard
│   │   │           ├── DashboardApplication.java
│   │   │           └── doctor_dashboard
│   │   │               ├── controllers
│   │   │               │   ├── AppointmentController.java
│   │   │               │   ├── DoctorController.java
│   │   │               │   ├── FileController.java
│   │   │               │   ├── LoginController.java
│   │   │               │   ├── PatientController.java
│   │   │               │   ├── PrescriptionController.java
│   │   │               │   ├── ReceptionistController.java
│   │   │               │   └── TodoController.java
│   │   │               ├── entities
│   │   │               │   ├── dtos
│   │   │               │   │   ├── AppointmentDto.java
│   │   │               │   │   ├── AppointmentViewDto.java
│   │   │               │   │   ├── AttributesDto.java
│   │   │               │   │   ├── DoctorAppointmentListDto.java
│   │   │               │   │   ├── DoctorBasicDetailsDto.java
│   │   │               │   │   ├── DoctorDropdownDto.java
│   │   │               │   │   ├── DoctorFormDto.java
│   │   │               │   │   ├── DoctorListDto.java
│   │   │               │   │   ├── FollowUpDto.java
│   │   │               │   │   ├── NotificationDto.java
│   │   │               │   │   ├── PatientAppointmentListDto.java
│   │   │               │   │   ├── PatientDetailsUpdateDto.java
│   │   │               │   │   ├── PatientDto.java
│   │   │               │   │   ├── PatientEntityDto.java
│   │   │               │   │   ├── PatientListDto.java
│   │   │               │   │   ├── PatientProfileDto.java
│   │   │               │   │   ├── PatientViewDto.java
│   │   │               │   │   ├── TodoListDto.java
│   │   │               │   │   └── UpdatePrescriptionDto.java
│   │   │               │   ├── login_entity
│   │   │               │   │   ├── JwtToken.java
│   │   │               │   │   └── LoginDetails.java
│   │   │               │   ├── model
│   │   │               │   │   ├── Appointment.java
│   │   │               │   │   ├── Attributes.java
│   │   │               │   │   ├── DoctorDetails.java
│   │   │               │   │   ├── Patient.java
│   │   │               │   │   ├── Prescription.java
│   │   │               │   │   └── Todolist.java
│   │   │               │   └── report
│   │   │               │       ├── FileDB.java
│   │   │               │       └── ResponseMessage.java
│   │   │               ├── exceptions
│   │   │               │   ├── APIException.java
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── GoogleLoginException.java
│   │   │               │   ├── InvalidDate.java
│   │   │               │   ├── MailErrorException.java
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   └── ValidationsException.java
│   │   │               ├── jwt
│   │   │               │   ├── config
│   │   │               │   │   └── SecurityConfig.java
│   │   │               │   ├── entities
│   │   │               │   │   ├── AuthenticationResponse.java
│   │   │               │   │   ├── Claims.java
│   │   │               │   │   └── Login.java
│   │   │               │   ├── security
│   │   │               │   │   ├── CustomAuthenticationEntryPoint.java
│   │   │               │   │   ├── CustomUserDetailsService.java
│   │   │               │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │               │   │   ├── JwtAuthenticationFilter.java
│   │   │               │   │   ├── JwtTokenProvider.java
│   │   │               │   │   └── MyUserDetails.java
│   │   │               │   └── service
│   │   │               │       ├── JwtServiceImpl.java
│   │   │               │       └── JwtService.java
│   │   │               ├── repository
│   │   │               │   ├── AppointmentRepository.java
│   │   │               │   ├── AttributeRepository.java
│   │   │               │   ├── DoctorRepository.java
│   │   │               │   ├── FileDBRepository.java
│   │   │               │   ├── LoginRepo.java
│   │   │               │   ├── PatientRepository.java
│   │   │               │   ├── PrescriptionRepository.java
│   │   │               │   └── TodoRepository.java
│   │   │               ├── services
│   │   │               │   ├── appointment_service
│   │   │               │   │   ├── AppointmentServiceImpl.java
│   │   │               │   │   └── AppointmentService.java
│   │   │               │   ├── doctor_service
│   │   │               │   │   ├── DoctorServiceImpl.java
│   │   │               │   │   └── DoctorService.java
│   │   │               │   ├── login_service
│   │   │               │   │   ├── LoginServiceImpl.java
│   │   │               │   │   └── LoginService.java
│   │   │               │   ├── patient_service
│   │   │               │   │   ├── impl
│   │   │               │   │   │   ├── FileStorageService.java
│   │   │               │   │   │   └── PatientServiceImpl.java
│   │   │               │   │   └── PatientService.java
│   │   │               │   ├── prescription_service
│   │   │               │   │   ├── PrescriptionServiceImpl.java
│   │   │               │   │   └── PrescriptionService.java
│   │   │               │   ├── receptionist
│   │   │               │   │   ├── ReceptionistServiceImpl.java
│   │   │               │   │   └── ReceptionistService.java
│   │   │               │   └── todo_service
│   │   │               │       ├── TodoServiceImpl.java
│   │   │               │       └── TodoService.java
│   │   │               └── utils
│   │   │                   ├── Constants.java
│   │   │                   ├── MailServiceImpl.java
│   │   │                   ├── PdFGeneratorServiceImpl.java
│   │   │                   └── wrapper
│   │   │                       ├── ErrorDetails.java
│   │   │                       ├── ErrorMessage.java
│   │   │                       ├── GenericMessage.java
│   │   │                       └── ValidationsSchema.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── db
│   │           └── migration
│   │               ├── V1.1__newTable.sql
│   │               ├── V2__info1.sql
│   │               └── V3__deleteFiles.sql)
```
