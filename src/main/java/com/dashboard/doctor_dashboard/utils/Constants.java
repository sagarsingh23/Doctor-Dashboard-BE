package com.dashboard.doctor_dashboard.utils;


public class Constants {
    public static final String SUCCESS = "success";
    public static final String FAIL = "failure";
    public static final String LOGIN = "Login";
    public static final String  DEFAULT_PAGE_SIZE="10";
    public static final String  UNAUTHORIZED="Authentication failed,token is not valid";


    public static final String DETAILS_MISMATCH = "details provided is not correct";

    public static final String FILE_NAME_PRESENT = "file name is empty";
    public static final String FILE_UPLOADED = "Uploaded the file successfully: ";

    public static final String REPORT_NOT_FOUND = "No Report Found!! ";



    //Login
    public static final String LOGIN_DETAILS_NOT_FOUND = "login details not found with the id provided.";


    //DOCTOR
    public static final String DOCTOR_NOT_FOUND = "doctor not found with the id provided.";
    public static final String TODO_NOT_FOUND = "todo not found with the id provided.";

    public static final String DOCTOR_NOT_FOUND_SPECIALITY = "doctor not found with the speciality provided.";


    //PATIENT
    public static final String PATIENT_NOT_FOUND = "Patient not found with the id provided.";


    //APPOINTMENT
    public static final String APPOINTMENT_NOT_FOUND = "appointment not found with the id provided.";
    public static final String APPOINTMENT_CREATED = "appointment successfully created";
    public static final String APPOINTMENT_ALREADY_BOOKED = "appointment is already booked for this time, please refresh.";
    public static final String APPOINTMENT_CANNOT_BE_BOOKED = "appointment cannot be booked on this date";
    public static final String SELECT_SPECIFIED_DATES = "select dates from specified range";

    public static final String INVALID_PATIENT_NAME = "invalid patient name";
    public static final String INVALID_PATIENT_EMAIL = "invalid patient email";
    public static final String INVALID_DOCTOR_NAME = "invalid doctor name";

    public static final String MAIL_ERROR = "mail error!!! cant connect with the host server";
    public static final String PRESCRIPTION_CREATED = "prescription added";

    private Constants() {
        throw new IllegalArgumentException();
    }
}
