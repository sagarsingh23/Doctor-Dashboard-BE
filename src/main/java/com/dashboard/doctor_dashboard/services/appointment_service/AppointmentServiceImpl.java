package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.model.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.utils.wrapper.GenericMessage;
import com.dashboard.doctor_dashboard.exceptions.InvalidDate;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import com.dashboard.doctor_dashboard.utils.Constants;
import com.dashboard.doctor_dashboard.utils.MailServiceImpl;
import com.dashboard.doctor_dashboard.utils.PdFGeneratorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  AppointmentServiceImpl
 */
@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final PdFGeneratorServiceImpl pdFGeneratorService;
    private final LoginRepo loginRepo;
    private final MailServiceImpl mailService;
    private  final JwtTokenProvider jwtTokenProvider;
    private final ModelMapper mapper;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, PdFGeneratorServiceImpl pdFGeneratorService, LoginRepo loginRepo, MailServiceImpl mailService, JwtTokenProvider jwtTokenProvider, ModelMapper mapper) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.pdFGeneratorService = pdFGeneratorService;
        this.loginRepo = loginRepo;
        this.mailService = mailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.mapper = mapper;
    }


    private  static Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private static final  Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();

    static final List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
    List<String> times=Arrays.asList("10:00","10:30","11:00","11:30","12:00","12:30","14:00","14:30","15:00","15:30","16:00","16:30");         // list of timeslots for doctor


    /**
     * This function of service  is for adding appointment details
     * @param appointment  this variable contains appointment details.
     * @param request  this variable contains request.
     * @return It returns a ResponseEntity<GenericMessage> with status code 201.
     * @throws MessagingException
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    @Override
    public ResponseEntity<GenericMessage>  addAppointment(AppointmentDto appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {
        Map<String,String> m = new HashMap<>();
        Long loginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(loginId) != null) { //checking if the patient exists database.
            Long patientId=patientRepository.getId(appointment.getPatient().getPID());
            if ( patientId!= null && doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId()) != null) { //checking if the doctor and patient details exists in database.
                checkSanityOfAppointment(mapper.map(appointment,Appointment.class)); //cross-checking the values inside the object with the database.
                appointment.getPatient().setPID(patientId);
                LocalDate appDate=appointment.getDateOfAppointment();
                if(appDate.isAfter(LocalDate.now())&&appDate.isBefore(LocalDate.now().plusDays(8))) { //checking if the date of appointment is a future date and in one week range from current date.
                    if(Boolean.TRUE.equals(appointment.getIsBookedAgain())) { //checking if the appointment is a follow-up appointment,
                        bookAgainHandler(mapper.map(appointment,Appointment.class)); // checking if a previous appointment exist for the follow-up appointment.
                    }
                    isAppointmentTimeValid(mapper.map(appointment,Appointment.class)); //checking if the selected appointment time is valid and is the slot empty.
                    appointment.setStatus("To be attended");
                    var appointment1 = appointmentRepository.save(mapper.map(appointment,Appointment.class));

                    log.debug("appointment service::"+ Constants.APPOINTMENT_CREATED);

                    m.put("appointId",appointment1.getAppointId().toString());
                    m.put("message",Constants.APPOINTMENT_CREATED);
                    sendEmailToUser(mapper.map(appointment,Appointment.class));          //  sending mail to user after successful booking of appointment
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,m),HttpStatus.CREATED);
                }
                logger.info(Constants.APPOINTMENT_CANNOT_BE_BOOKED);
                throw new InvalidDate(appDate+":"+Constants.APPOINTMENT_CANNOT_BE_BOOKED);             //appointment cannot be booked on this date
            }
            else if(patientId == null){

                logger.info(Constants.PATIENT_NOT_FOUND);
                throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);                     // patient Not Found with id provided
            }
            else {
                logger.info(Constants.DOCTOR_NOT_FOUND);
                throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);                      //  Doctor Not Found with id provided
            }
        }
        logger.info(Constants.LOGIN_DETAILS_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.LOGIN_DETAILS_NOT_FOUND);                       //  login details Not Found with id provided
    }

    /**
     * This function of service is for checking of previous appointment for follow-up appointment
     * @param appointment this variable contains appointment details.
     */
    private void bookAgainHandler(Appointment appointment){                   // checks if previous appointment exists for the follow-Up appointment
        log.info("inside: appointment service::bookAgainHandler");
        if(appointment.getFollowUpAppointmentId()!=null && appointmentRepository.existsById(appointment.getFollowUpAppointmentId())) {
            var getAppointmentById = appointmentRepository.getAppointmentById(appointment.getFollowUpAppointmentId());
            if (!appointment.getPatient().getPID().equals(getAppointmentById.getPatient().getPID())) {
                log.info("appointment service::bookAgainHandler"+Constants.APPOINTMENT_NOT_FOUND);
                throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
            }
            appointmentRepository.changeFollowUpStatus(appointment.getFollowUpAppointmentId());
            appointment.setIsBookedAgain(null);
        }
        else {
            log.error("appointment service::bookAgainHandler"+Constants.APPOINTMENT_NOT_FOUND);
            throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
        }
    }

    /**
     * This function of service is for checking appointment time valid or not
     * @param appointment this variable contains appointment details.
     */
    private void isAppointmentTimeValid(Appointment appointment){      //checks if the selected appointment time is valid and is the slot empty.
        log.info("inside: appointment service::isAppointmentTimeValid");
        var time=appointment.getAppointmentTime().toString();
        int index = times.indexOf(time);
        if(index==-1)
            throw new InvalidDate(time+":Invalid time");
        List<Boolean> c = new ArrayList<>(checkSlots(appointment.getDateOfAppointment(), appointment.getDoctorDetails().getId())); // checking if the selected time slot is empty.
        if(Boolean.TRUE.equals(c.get(index))) {
            c.set(index, false);                //            log.info("appointment service::checkSanityOfAppointment"+c.toString())
        }else {
            log.info("appointment service::isAppointmentTimeValid"+Constants.APPOINTMENT_ALREADY_BOOKED);
            throw new InvalidDate(time+":"+Constants.APPOINTMENT_ALREADY_BOOKED);
        }
        slots.get(appointment.getDoctorDetails().getId()).put(appointment.getDateOfAppointment(), c);
        log.info("exit: appointment service::isAppointmentTimeValid");
    }

    /**
     * This function of service is for checking data inside database
     * @param appointment this variable contains appointment details.
     */
    public void checkSanityOfAppointment(Appointment appointment){ //cross-checks the values inside the object with the database.

        log.info("inside: appointment service::checkSanityOfAppointment");

        long patientId=appointment.getPatient().getPID();
        long doctorId=appointment.getDoctorDetails().getId();

        Optional<LoginDetails> patientDetails= loginRepo.findById(patientId);
        Optional<LoginDetails> doctorDetails= loginRepo.findById(doctorId);

        if(patientDetails.isPresent() && doctorDetails.isPresent()) {

            var patientLoginDetails = patientDetails.get();
            var doctorLoginDetails = doctorDetails.get();

            if (!patientLoginDetails.getEmailId().equals(appointment.getPatientEmail())) {
                log.info("appointment service::checkSanityOfAppointment - Invalid Patient Email"+Constants.INVALID_PATIENT_EMAIL);
                log.info(new ArrayList<>(List.of(Constants.INVALID_PATIENT_EMAIL)).toString());
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_PATIENT_EMAIL)));
            }
            if (!patientLoginDetails.getName().equals(appointment.getPatientName())) {

                log.info("appointment service::checkSanityOfAppointment  - Invalid Patient Name"+Constants.INVALID_PATIENT_NAME);
                log.info(new ArrayList<>(List.of(Constants.INVALID_PATIENT_NAME)).toString());
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_PATIENT_NAME)));
            }

            if (!doctorLoginDetails.getName().equals(appointment.getDoctorName())) {
                log.info("appointment service::checkSanityOfAppointment - Invalid Doctor Name"+Constants.INVALID_DOCTOR_NAME);
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_DOCTOR_NAME)));
            }
        }
        log.info("exit: appointment service::checkSanityOfAppointment");
    }

    /**
     * This function of service of getting all appointment by patient
     * @param loginId this variable contains login id.
     * @param pageNo this variable contains Page no.
     * @return It returns a ResponseEntity<GenericMessage> with status code 201.
     */
    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long loginId, int pageNo){
        log.info("inside: appointment service::getAllAppointmentByPatientId");
        var genericMessage = new GenericMessage();
        List<PatientAppointmentListDto> today = new ArrayList<>();
        Map<String,List<PatientAppointmentListDto>> m = new HashMap<>();
        Pageable paging= PageRequest.of(pageNo, 10);

        Long patientId=patientRepository.getId(loginId);
        if(patientId != null) {
            List<PatientAppointmentListDto> past = mapToAppointList(appointmentRepository.pastAppointment(patientId,paging).toList());
            List<PatientAppointmentListDto> upcoming = mapToAppointList(appointmentRepository.upcomingAppointment(patientId,paging).toList());
            List<PatientAppointmentListDto> today1 = mapToAppointList(appointmentRepository.todayAppointment1(patientId,paging).toList());
            List<PatientAppointmentListDto> today2 = mapToAppointList(appointmentRepository.todayAppointment2(patientId,paging).toList());
            today.addAll(today1);
            today.addAll(today2);

            m.put("past",past);
            m.put("today",today);
            m.put("upcoming",upcoming);

            genericMessage.setData(m);
            genericMessage.setStatus(Constants.SUCCESS);


            log.info("exit: appointment service::getAllAppointmentByPatientId");

            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }

      logger.info(Constants.PATIENT_NOT_FOUND);
      throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
}

    /**
     * This function of service of getting all appointment by doctor
     * @param loginId this variable contains login id.
     * @param pageNo this variable contains Page no.
     * @return It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(Long loginId,int pageNo) {
        log.info("inside: appointment service::allAppointmentByDoctorId");

        var genericMessage = new GenericMessage();
        List<DoctorAppointmentListDto> today = new ArrayList<>();
        Map<String,List<DoctorAppointmentListDto>> m = new HashMap<>();
        Long doctorId = doctorRepository.isIdAvailable(loginId);
        Pageable paging= PageRequest.of(pageNo, 10);


        if(doctorId != null) {
            List<DoctorAppointmentListDto> past = mapToAppointDoctorList(appointmentRepository.pastDoctorAppointment(doctorId,paging).toList());
            List<DoctorAppointmentListDto> upcoming = mapToAppointDoctorList(appointmentRepository.upcomingDoctorAppointment(doctorId,paging).toList());
            List<DoctorAppointmentListDto> today1 = mapToAppointDoctorList(appointmentRepository.todayDoctorAppointment1(doctorId,paging).toList());
            List<DoctorAppointmentListDto> today2 = mapToAppointDoctorList(appointmentRepository.todayDoctorAppointment2(doctorId,paging).toList());
            today.addAll(today1);
            today.addAll(today2);

            m.put("past",past);
            m.put("today",today);
            m.put("upcoming",upcoming);

            genericMessage.setData(m);
            genericMessage.setStatus(Constants.SUCCESS);

            log.info("exit: appointment service::allAppointmentByDoctorId");
            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
        logger.info("inside: appointment service::allAppointmentByDoctorId"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * @param appointId this variable contains appointment details.
     * @return It returns a ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getFollowDetails(Long appointId){
        log.info("inside: appointment service::getFollowDetails");

        if(appointmentRepository.getId(appointId) != null && appointId.equals(appointmentRepository.getId(appointId))){
            log.info("exit: appointment service::getFollowDetails");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,mapper.map(appointmentRepository.getFollowUpData(appointId),FollowUpDto.class)),HttpStatus.OK);
        }
        log.info("appointment service::getFollowDetails"+Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);
    }

    /**
     * @param appointId this variable contains appointment details.
     * @return It returns a  ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> getAppointmentById(Long appointId){
        log.info("inside: appointment service::getAppointmentById");
        if(appointmentRepository.getId(appointId) != null){
            var appointment = appointmentRepository.getAppointmentById(appointId);
            log.info("exit: appointment service::getAppointmentById");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,mapper.map(appointment,PatientProfileDto.class)),HttpStatus.OK);
        }
        log.info("appointment service::getAppointmentById"+Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.APPOINTMENT_NOT_FOUND);

    }
    /**
     * @param doctorId this variable contains appointment details.
     * @return  It returns a  ResponseEntity<GenericMessage> for weeklyDoctorCountChart.
     */
    @Override
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(Long doctorId){
        log.info("inside: appointment service::weeklyDoctorCountChart");
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByDoctorId(doctorId);
            log.info("exit: appointment service::weeklyDoctorCountChart");

            return weeklyGraph(dateList);
        }
        log.info("appointment service::weeklyDoctorCountChart"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * @param patientId this variable contains patient Id.
     * @return  It returns a  ResponseEntity<GenericMessage> for  weeklyPatientCountChart
     */
    @Override
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(Long patientId){
        log.info("inside: appointment service::weeklyPatientCountChart");
        Long id = patientRepository.getId(patientId);
        if(id != null) {
            ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByPatientId(id);
            log.info("exit: appointment service::weeklyPatientCountChart");

            return weeklyGraph(dateList);
        }
        log.info("appointment service::weeklyPatientCountChart"+Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);
    }

    /**
     * @param doctorId this variable contains doctor Id.
     * @return It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> recentAppointment(Long doctorId){

        log.info("inside: appointment service::recentAppointment");
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            List<Appointment> appointments = appointmentRepository.recentAppointment(doctorId);
            List<DoctorAppointmentListDto> list =  appointments.stream()
                    .map(this::mapToDto).collect(Collectors.toList());
            log.info("exit: appointment service::recentAppointment");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list),HttpStatus.OK);
        }
        log.info("appointment service::recentAppointment"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * @param doctorId this variable contains doctor Id.
     * @return It returns ResponseEntity<GenericMessage> with status code 200.
     */
    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId){
        log.info("inside: appointment service::totalNoOfAppointment");

        if(doctorRepository.isIdAvailable(doctorId) != null) {
            log.info("exit: appointment service::totalNoOfAppointment");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.totalNoOfAppointment(doctorId)), HttpStatus.OK);
        }
        log.info("appointment service::totalNoOfAppointment"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }
    /**
     * @param doctorId this variable contains doctor Id.
     * @return  It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> todayAppointments(Long doctorId){
        log.info("inside: appointment service::todayAppointments");

        if(doctorRepository.isIdAvailable(doctorId) != null) {
            log.info("exit: appointment service::todayAppointments");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.todayAppointments(doctorId)), HttpStatus.OK);
        }
        log.info("appointment service::todayAppointments"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }
    /**
     * @param doctorId  this variable contains doctor Id.
     * @return  It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId){
        log.info("inside: appointment service::totalNoOfAppointmentAddedThisWeek");

        if(doctorRepository.isIdAvailable(doctorId) != null) {
            log.info("exit: appointment service::totalNoOfAppointmentAddedThisWeek");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.totalNoOfAppointmentAddedThisWeek(doctorId)), HttpStatus.OK);
        }
        log.info("appointment service::totalNoOfAppointmentAddedThisWeek"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }
    /**
     * @param loginId this variable contains login Id.
     * @return  It returns ResponseEntity<GenericMessage> with status code 200 .
     */
    @Override
    public ResponseEntity<GenericMessage> patientCategoryGraph(Long loginId){
        log.info("inside: appointment service::patientCategoryGraph");

        Long patientId = patientRepository.getId(loginId);
        if(patientId != null) {
            log.info("exit: appointment service::patientCategoryGraph");

            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.patientCategoryGraph(patientId)), HttpStatus.OK);
        }
        log.info("appointment service::patientCategoryGraph"+Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.PATIENT_NOT_FOUND);

    }
    /**
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return It returns Map<Long,Map<LocalDate,List<Boolean>>> for  checkSlotsAvail
     */
    public Map<Long,Map<LocalDate,List<Boolean>>> checkSlotsAvail(LocalDate date,Long doctorId){        //checking if the slots of  doctor in the DB and adding to Map.

        log.info("inside: appointment service::checkSlotsAvail");      //checking if the slots of  doctor in the DB and adding to Map.

        Map<LocalDate,List<Boolean>> dateAndTime=new HashMap<>();
        List<Boolean> docTimesSlots=new ArrayList<>(Collections.nCopies(12,true));
        List<LocalTime> doctorBookedSlots;
        List<Time> dates=appointmentRepository.getTimesByIdAndDate(date,doctorId);
        doctorBookedSlots=dates
                .stream()
                .map(Time::toLocalTime).collect(Collectors.toList());
        if(!doctorBookedSlots.isEmpty()){
            for (LocalTime doctorBookedSlot : doctorBookedSlots) {
                docTimesSlots.set(times.indexOf(doctorBookedSlot.toString()), false);
            }
            if(slots.get(doctorId)==null){
                dateAndTime.put(date,docTimesSlots);
                slots.put(doctorId, dateAndTime);
            }
            else {
                slots.get(doctorId).putIfAbsent(date, docTimesSlots);
            }
            log.info("exit1: appointment service::checkSlotsAvail");

            return slots;
        }
        if(slots.get(doctorId)==null){
            dateAndTime.put(date,timesSlots);
            slots.put(doctorId, dateAndTime);

        }
        else {
            slots.get(doctorId).computeIfAbsent(date, k -> timesSlots);
        }
        log.info("exit2: appointment service::checkSlotsAvail");

        return slots;
    }



    /**
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return  It returns List<Boolean> for checkSlots.
     */
    public List<Boolean> checkSlots(LocalDate date,Long doctorId){
        log.info("inside: appointment service::checkSlots");
        if(doctorRepository.isIdAvailable(doctorId)!=null) {

            if(slots.get(doctorId) != null) {                              //checking if the doctorId present in the map.
                log.info("exit1: appointment service::checkSlots");

                return mainIfFunction(date, doctorId);
            }

            else {
                log.info("exit2: appointment service::checkSlots");

                return mainElseFunction(date, doctorId);
            }

        }
        log.info("appointment service::checkSlots"+Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFoundException(Constants.DOCTOR_NOT_FOUND);
    }

    /**
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return  It returns List<Boolean> for mainIfFunction.
     */
    List<Boolean> mainIfFunction(LocalDate date, Long doctorId){
        log.info("inside: appointment service::mainIfFunction");

        if (Boolean.TRUE.equals(pdFGeneratorService.dateHandler(date))) {
            if (slots.get(doctorId).get(date) != null) { //checking if the doctor slot details present in the map.
                log.info("exit1: appointment service::mainIfFunction");

                return slots.get(doctorId).get(date);
            } else {
                log.info("exit2: appointment service::mainIfFunction");

                return checkSlotsAvail(date, doctorId).get(doctorId).get(date); //checking if the slots of  doctor in the DB and adding to Map.
            }
        }else {
            log.info("appointment service::mainIfFunction"+Constants.SELECT_SPECIFIED_DATES);
            throw new InvalidDate(date+":"+Constants.SELECT_SPECIFIED_DATES);
        }

    }

    /**
     * @param date this variable contains date.
     * @param doctorId this variable contains doctor Id.
     * @return  It returns List<Boolean> for mainIfFunction.
     */
    List<Boolean> mainElseFunction(LocalDate date, Long doctorId){
        log.info("inside: appointment service::mainElseFunction");

        if (Boolean.TRUE.equals(pdFGeneratorService.dateHandler(date))){
            log.info("exit: appointment service::mainElseFunction");

            return checkSlotsAvail(date, doctorId).get(doctorId).get(date);
        }
        else {
            log.info("appointment service::mainElseFunction"+Constants.SELECT_SPECIFIED_DATES);
            throw new InvalidDate(date+":"+Constants.SELECT_SPECIFIED_DATES);
        }
    }



    /**
     * @param appointments  this variable contains appointments.
     * @return It returns List<PatientAppointmentListDto>
     */
    List<PatientAppointmentListDto> mapToAppointList(List<Appointment> appointments){
        log.info("appointment service::mapToAppointList");
        return appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
    }

    /**
     * @param appointments  this variable contains appointments.
     * @return It returns List<DoctorAppointmentListDto>
     */
    List<DoctorAppointmentListDto> mapToAppointDoctorList(List<Appointment> appointments){
        log.info("appointment service::mapToAppointDoctorList");

        return appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    /**
     * @param appointment  this variable contains appointments.
     * @return  It returns DoctorAppointmentListDto.
     */
    private DoctorAppointmentListDto mapToDto(Appointment appointment) {
        log.info("appointment service::mapToDto");

        return mapper.map(appointment, DoctorAppointmentListDto.class);
    }

    /**
     * @param appointment this variable contains appointments.
     * @return  It returns PatientAppointmentListDto.
     */
    private PatientAppointmentListDto mapToDto2(Appointment appointment) {
        log.info("appointment service::mapToDto2");

        return mapper.map(appointment, PatientAppointmentListDto.class);
    }

    /**
     * @param appointment this variable contains appointments.
     * @throws JSONException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public void sendEmailToUser(Appointment appointment) throws JSONException, MessagingException, UnsupportedEncodingException {
        log.info("inside: appointment service::sendEmailToUser");

        String doctorEmail = loginRepo.email(appointment.getDoctorDetails().getId());
        String toEmail = appointment.getPatientEmail();
        var fromEmail = "mecareapplication@gmail.com";
        var senderName = "meCare Team";
        var subject = "Appointment Confirmed";

        String content = Constants.MAIL_APPOINTMENT;      // here Mail Appointment contains Html Content

        content = content.replace("[[name]]", appointment.getPatientName());
        content = content.replace("[[doctorName]]", appointment.getDoctorName());

        content = content.replace("[[doctorEmail]]", doctorEmail);
        content = content.replace("[[speciality]]", appointment.getCategory());

        content = content.replace("[[dateOfAppointment]]",pdFGeneratorService.formatDate(appointment.getDateOfAppointment().toString()));

        content = content.replace("[[appointmentTime]]", appointment.getAppointmentTime().toString());

        log.info("exit: appointment service::sendEmailToUser");
        mailService.mailServiceHandler(fromEmail,toEmail,senderName,subject,content);
    }

    /**
     * @param dateList this variable contains Date details.
     * @return It returns ResponseEntity<GenericMessage> with status code 200.
     */
    ResponseEntity<GenericMessage> weeklyGraph(ArrayList<java.sql.Date> dateList){

        log.info("inside: appointment service::weeklyGraph");

        int lengthOfMonth = LocalDate.now().lengthOfMonth();
        ArrayList<String> newList = new ArrayList<>();
        var year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        Calendar.getInstance().get(Calendar.MONTH);
        var month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        month = pdFGeneratorService.monthHandler(month);
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



        ArrayList<LocalDate> localDateList =new ArrayList<>();
        for (java.sql.Date date : dateList) {
            localDateList.add(date.toLocalDate());
        }

        for (LocalDate localDate : localDateList) {
            if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "08"))) {
                firstWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "15"))) {
                secondWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "22"))) {
                thirdWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + "29"))) {
                fourthWeekCount++;
            } else if (localDate.isBefore(LocalDate.parse(year + "-" + month + "-" + lengthOfMonth))) {
                lastWeekCount++;
            } else if (localDate.equals(LocalDate.parse(year + "-" + month + "-" + lengthOfMonth))) {
                lastWeekCount++;
            } else{
                log.info("appointment service::weeklyGraph Invalid date");
                throw new InvalidDate("Invalid date");
            }
        }
        newList.add(firstWeek+","+firstWeekCount);
        newList.add(secondWeek+","+secondWeekCount);
        newList.add(thirdWeek+","+thirdWeekCount);
        newList.add(fourthWeek+","+fourthWeekCount);
        newList.add(lastWeek+","+lastWeekCount);
        log.info("exit: appointment service::weeklyGraph");
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,newList),HttpStatus.OK);
    }

}
