package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.exceptions.InvalidDate;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFound;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private LoginRepo loginRepo;


    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JavaMailSender mailSender;

    private  static Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private static final  Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();

    static final List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
    List<String> times=Arrays.asList("10:00","10:30","11:00","11:30","12:00","12:30","14:00","14:30","15:00","15:30","16:00","16:30");


    @Autowired
    private ModelMapper mapper;


    @Override
    public ResponseEntity<GenericMessage>  addAppointment(Appointment appointment, HttpServletRequest request) throws MessagingException, JSONException, UnsupportedEncodingException {
        Map<String,String> m = new HashMap<>();
        Long loginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(loginId) != null) {
            Long patientId=patientRepository.getId(appointment.getPatient().getPID());
            if ( patientId!= null && doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId()) != null) {
                checkSanityOfAppointment(appointment);
                appointment.getPatient().setPID(patientId);
                LocalDate appDate=appointment.getDateOfAppointment();
                if(appDate.isAfter(LocalDate.now())&&appDate.isBefore(LocalDate.now().plusDays(8))) {

                    if(appointment.getIsBookedAgain()!=null&& appointment.getIsBookedAgain()) {
                        if(appointment.getFollowUpAppointmentId()!=null && appointmentRepository.existsById(appointment.getFollowUpAppointmentId())) {
                            var getAppointmentById = appointmentRepository.getAppointmentById(appointment.getFollowUpAppointmentId());
                            if (!appointment.getPatient().getPID().equals(getAppointmentById.getPatient().getPID())) {
                                logger.info(Constants.APPOINTMENT_NOT_FOUND);
                                throw new ResourceNotFound(Constants.APPOINTMENT_NOT_FOUND);
                            }
                            appointmentRepository.changeFollowUpStatus(appointment.getFollowUpAppointmentId());
                            appointment.setIsBookedAgain(null);
                        }
                        else {
                            logger.info(Constants.APPOINTMENT_NOT_FOUND);
                            throw new ResourceNotFound(Constants.APPOINTMENT_NOT_FOUND);
                        }
                    }
                    List<Boolean> c = new ArrayList<>(checkSlots(appointment.getDateOfAppointment(), appointment.getDoctorDetails().getId()));
                    var time=appointment.getAppointmentTime().toString();
                    int index = times.indexOf(time);
                        if(index==-1)
                            throw new InvalidDate(appointment.getAppointmentTime().toString(),"Invalid time");
                    if(Boolean.TRUE.equals(c.get(index))) {
                            c.set(index, false);
                    }else {
                        logger.info(Constants.APPOINTMENT_ALREADY_BOOKED);
                        throw new InvalidDate(appointment.getAppointmentTime().toString(),Constants.APPOINTMENT_ALREADY_BOOKED);
                    }
                    slots.get(appointment.getDoctorDetails().getId()).put(appointment.getDateOfAppointment(), c);
                    appointment.setStatus("To be attended");
                    appointmentRepository.save(appointment);

                    log.debug("appointment service::"+Constants.APPOINTMENT_CREATED);

                    m.put("appointId",appointment.getAppointId().toString());
                    m.put("message",Constants.APPOINTMENT_CREATED);
                    sendEmailToUser(appointment);
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,m),HttpStatus.OK);
                }
                logger.info(Constants.APPOINTMENT_CANNOT_BE_BOOKED);
                throw new InvalidDate(appDate.toString(),Constants.APPOINTMENT_CANNOT_BE_BOOKED);
            }
            else if(patientId!=null){

                logger.info(Constants.PATIENT_NOT_FOUND);
                throw new ResourceNotFound(Constants.PATIENT_NOT_FOUND);
            }
            else if (doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId()) == null) {

                logger.info(Constants.DOCTOR_NOT_FOUND);
                throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
            }
        }
        logger.info(Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFound(Constants.PATIENT_NOT_FOUND);
    }

    private void checkSanityOfAppointment(Appointment appointment){

        long patientId=appointment.getPatient().getPID();
        long doctorId=appointment.getDoctorDetails().getId();

        Optional<LoginDetails> patientDetails= loginRepo.findById(patientId);
        Optional<LoginDetails> doctorDetails= loginRepo.findById(doctorId);

        if(patientDetails.isPresent() && doctorDetails.isPresent()) {

            var patientLoginDetails = patientDetails.get();
            var doctorLoginDetails = doctorDetails.get();

            if (!patientLoginDetails.getEmailId().equals(appointment.getPatientEmail())) {

                logger.info(Constants.INVALID_PATIENT_EMAIL);
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_PATIENT_EMAIL)));
            }
            if (!patientLoginDetails.getName().equals(appointment.getPatientName())) {

                logger.info(Constants.INVALID_PATIENT_NAME);
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_PATIENT_NAME)));
            }
            if (!doctorLoginDetails.getName().equals(appointment.getDoctorName())) {

                logger.info(Constants.INVALID_DOCTOR_NAME);
                throw new ValidationsException(new ArrayList<>(List.of(Constants.INVALID_DOCTOR_NAME)));
            }
        }
    }

    public Map<Long,Map<LocalDate,List<Boolean>>> returnMap(){
        return slots;
    }

    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long loginId) {

        var genericMessage = new GenericMessage();
        List<PatientAppointmentListDto> today = new ArrayList<>();
        Map<String,List<PatientAppointmentListDto>> m = new HashMap<>();

        Long patientId=patientRepository.getId(loginId);
        if(patientId != null) {
        List<PatientAppointmentListDto> past = mapToAppointList(appointmentRepository.pastAppointment(patientId));
        List<PatientAppointmentListDto> upcoming = mapToAppointList(appointmentRepository.upcomingAppointment(patientId));
        List<PatientAppointmentListDto> today1 = mapToAppointList(appointmentRepository.todayAppointment1(patientId));
        List<PatientAppointmentListDto> today2 = mapToAppointList(appointmentRepository.todayAppointment2(patientId));
        today.addAll(today1);
        today.addAll(today2);

        m.put("past",past);
        m.put("today",today);
        m.put("upcoming",upcoming);

        genericMessage.setData(m);
        genericMessage.setStatus(Constants.SUCCESS);


      return new ResponseEntity<>(genericMessage,HttpStatus.OK);
     }

      logger.info(Constants.PATIENT_NOT_FOUND);
      throw new ResourceNotFound(Constants.PATIENT_NOT_FOUND);
}

    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(Long loginId) {

        var genericMessage = new GenericMessage();
        List<DoctorAppointmentListDto> today = new ArrayList<>();
        Map<String,List<DoctorAppointmentListDto>> m = new HashMap<>();
        Long doctorId = doctorRepository.isIdAvailable(loginId);

        if(doctorId != null) {
            List<DoctorAppointmentListDto> past = mapToAppointDoctorList(appointmentRepository.pastDoctorAppointment(doctorId));
            List<DoctorAppointmentListDto> upcoming = mapToAppointDoctorList(appointmentRepository.upcomingAppointment(doctorId));
            List<DoctorAppointmentListDto> today1 = mapToAppointDoctorList(appointmentRepository.todayAppointment1(doctorId));
            List<DoctorAppointmentListDto> today2 = mapToAppointDoctorList(appointmentRepository.todayAppointment2(doctorId));
            today.addAll(today1);
            today.addAll(today2);

            m.put("past",past);
            m.put("today",today);
            m.put("upcoming",upcoming);

            genericMessage.setData(m);
            genericMessage.setStatus(Constants.SUCCESS);


            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
        }
        logger.info(Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
    }


    @Override
    public ResponseEntity<GenericMessage> getFollowDetails(Long appointId) {
        if(appointmentRepository.getId(appointId) != null && appointId.equals(appointmentRepository.getId(appointId))){
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,mapper.map(appointmentRepository.getFollowUpData(appointId),FollowUpDto.class)),HttpStatus.OK);
        }
        logger.info(Constants.APPOINTMENT_NOT_FOUND);
        throw new ResourceNotFound(Constants.APPOINTMENT_NOT_FOUND);
    }


    @Override
    public PatientProfileDto getAppointmentById(Long appointId) {
        if(appointmentRepository.getId(appointId) != null){
        var appointment = appointmentRepository.getAppointmentById(appointId);
        return mapper.map(appointment,PatientProfileDto.class);
        }
        throw new ResourceNotFound(Constants.APPOINTMENT_NOT_FOUND);

    }

    @Override
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(Long doctorId) {
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByDoctorId(doctorId);
            return weeklyGraph(dateList);
        }
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
    }


    @Override
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(Long patientId) {
        Long id = patientRepository.getId(patientId);
        if(id != null) {
            ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByPatientId(id);
            return weeklyGraph(dateList);
        }
        logger.info(Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFound(Constants.PATIENT_NOT_FOUND);
    }


    @Override
    public ResponseEntity<GenericMessage> recentAppointment(Long doctorId) {
        if(doctorRepository.isIdAvailable(doctorId) != null) {
        List<Appointment> appointments = appointmentRepository.recentAppointment(doctorId);
        List<DoctorAppointmentListDto> list =  appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list),HttpStatus.OK);
        }
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
    }



    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId) {
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.totalNoOfAppointment(doctorId)), HttpStatus.OK);
        }
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
    }

    @Override
    public ResponseEntity<GenericMessage> todayAppointments(Long doctorId) {
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.todayAppointments(doctorId)), HttpStatus.OK);
        }
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
    }

    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId) {
        if(doctorRepository.isIdAvailable(doctorId) != null) {
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.totalNoOfAppointmentAddedThisWeek(doctorId)), HttpStatus.OK);
        }
        logger.info(Constants.DOCTOR_NOT_FOUND);
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);
    }

    @Override
    public ResponseEntity<GenericMessage> patientCategoryGraph(Long loginId) {
        Long patientId = patientRepository.getId(loginId);
        if(patientId != null) {
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.patientCategoryGraph(patientId)), HttpStatus.OK);
        }
        logger.info(Constants.PATIENT_NOT_FOUND);
        throw new ResourceNotFound(Constants.PATIENT_NOT_FOUND);

    }


    private Map<Long,Map<LocalDate,List<Boolean>>> checkSlotsAvail(LocalDate date,Long doctorId){
        Map<LocalDate,List<Boolean>> dateAndTime=new HashMap<>();

    List<Boolean> docTimesSlots=new ArrayList<>(Collections.nCopies(12,true));
        List<LocalTime> doctorBookedSlots;
        List<Time> dates=appointmentRepository.getTimesByIdAndDate(date,doctorId);
        doctorBookedSlots=dates.stream().map(Time::toLocalTime).collect(Collectors.toList());
        if(!doctorBookedSlots.isEmpty()){
            for (var i = 0; i < doctorBookedSlots.size(); i++) {
                docTimesSlots.set(times.indexOf(doctorBookedSlots.get(i).toString()),false);
            }

            if(slots.get(doctorId)==null){
                dateAndTime.put(date,docTimesSlots);
                slots.put(doctorId, dateAndTime);
                return slots;
            }
            if(slots.get(doctorId).get(date)==null){
                slots.get(doctorId).computeIfAbsent(date,k->docTimesSlots);
                return slots;

            }
            slots.get(doctorId).computeIfPresent(date,(k,v)->v=docTimesSlots);
            return slots;
        }
        if(slots.get(doctorId)==null){
            dateAndTime.put(date,timesSlots);
            slots.put(doctorId, dateAndTime);
            return slots;
        }
        if(slots.get(doctorId).get(date)==null){
            slots.get(doctorId).computeIfAbsent(date,k->timesSlots);
            return slots;
        }
        slots.get(doctorId).computeIfPresent(date,(k,v)->v=timesSlots);
        return slots;
    }

    public List<Boolean> checkSlots(LocalDate date,Long doctorId){
        if(doctorRepository.isIdAvailable(doctorId)!=null) {
            if (slots.get(doctorId) != null) {
                if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(8))) {

                    if (slots.get(doctorId).get(date) != null) {
                        return slots.get(doctorId).get(date);
                    } else {
                        return checkSlotsAvail(date, doctorId).get(doctorId).get(date);
                    }
                }else {
                    logger.info(Constants.SELECT_SPECIFIED_DATES);
                    throw new InvalidDate(date.toString(),Constants.SELECT_SPECIFIED_DATES);
                }
            }
            else{
                if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(8))) {
                    return checkSlotsAvail(date, doctorId).get(doctorId).get(date);
                }
                else {
                    logger.info(Constants.SELECT_SPECIFIED_DATES);
                    throw new InvalidDate(date.toString(),Constants.SELECT_SPECIFIED_DATES);
                }
            }
        }
        logger.info(Constants.SELECT_SPECIFIED_DATES);
        throw new ResourceNotFound(Constants.DOCTOR_NOT_FOUND);

    }

    List<PatientAppointmentListDto> mapToAppointList(List<Appointment> appointments){
        return appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());
    }

    List<DoctorAppointmentListDto> mapToAppointDoctorList(List<Appointment> appointments){
        return appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    private DoctorAppointmentListDto mapToDto(Appointment appointment) {
        return mapper.map(appointment, DoctorAppointmentListDto.class);
    }

    private PatientAppointmentListDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientAppointmentListDto.class);
    }

    public void sendEmailToUser(Appointment appointment) throws JSONException, MessagingException, UnsupportedEncodingException {
        log.info("Appointment Mail Service Started");

        String doctorEmail = loginRepo.email(appointment.getDoctorDetails().getId());
        String toEmail = appointment.getPatientEmail();
        var fromEmail = "mecareapplication@gmail.com";
        var senderName = "meCare Team";
        var subject = "Appointment Confirmed";

        String content = Constants.MAIL_APPOINTMENT;

        content = content.replace("[[name]]", appointment.getPatientName());
        content = content.replace("[[doctorName]]", appointment.getDoctorName());
        content = content.replace("[[doctorEmail]]", doctorEmail);
        content = content.replace("[[speciality]]", appointment.getCategory());
        content = content.replace("[[dateOfAppointment]]", formatDate(appointment.getDateOfAppointment().toString()));
        content = content.replace("[[appointmentTime]]", appointment.getAppointmentTime().toString());
        var obj = new JSONObject();
        obj.put("fromEmail", fromEmail);
        obj.put("toEmail", toEmail);
        obj.put("senderName", senderName);
        obj.put("subject", subject);
        obj.put("content", content);
        sendMailer(obj);
    }


    private void sendMailer(JSONObject obj) throws MessagingException, JSONException, UnsupportedEncodingException {

        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message);
            helper.setFrom(obj.get("fromEmail").toString(), obj.get("senderName").toString());
            helper.setTo(obj.get("toEmail").toString());
            helper.setText(obj.get("content").toString(), true);
            helper.setSubject(obj.get("subject").toString());
            mailSender.send(message);
            log.info(" Appointment Mail Service Stopped!!");

        }catch (Exception e)
        {
            logger.info(Constants.MAIL_ERROR);
            log.info("Exception!!! Mail Service Stopped!!");
            throw new ResourceNotFound(e.getMessage());
        }

    }

    String formatDate(String date1){
        try {
            String[] newArray = date1.split("-", 5);
            return newArray[2] + "-" + newArray[1] + "-" + newArray[0];
        }catch (Exception e){
            logger.info("date is null");
            throw  new NullPointerException("date is null");
        }

    }


    ResponseEntity<GenericMessage> weeklyGraph(ArrayList<java.sql.Date> dateList){

        int lengthOfMonth = LocalDate.now().lengthOfMonth();
        ArrayList<String> newList = new ArrayList<>();
        var year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        var month= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        if(Integer.parseInt(month)<10){
            month="0"+month;
        }
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

        for (var i=0;i<localDateList.size();i++)
        {
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-"+month+"-"+"01")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-"+month+"-"+"08")) || localDateList.get(i).equals(LocalDate.parse(year+"-"+month+"-"+"01"))){
                firstWeekCount++;

            }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-"+month+"-"+"08")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-"+month+"-"+"15")) || localDateList.get(i).equals(LocalDate.parse(year+"-"+month+"-"+"08"))){
                secondWeekCount++;
            }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-"+month+"-"+"15")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-"+month+"-"+"22"))|| localDateList.get(i).equals(LocalDate.parse(year+"-"+month+"-"+"15"))){
                thirdWeekCount++;
            }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-"+month+"-"+"22")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-"+month+"-"+"29"))|| localDateList.get(i).equals(LocalDate.parse(year+"-"+month+"-"+"22"))){
                fourthWeekCount++;
            }
            if(localDateList.get(i).isAfter(LocalDate.parse(year+"-"+month+"-"+"29")) && localDateList.get(i).isBefore(LocalDate.parse(year+"-"+month+"-"+lengthOfMonth)) || localDateList.get(i).equals(LocalDate.parse(year+"-"+month+"-"+"29")) ||localDateList.get(i).equals(LocalDate.parse(year+"-"+month+"-"+lengthOfMonth))){
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

}
