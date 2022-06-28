package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import com.dashboard.doctor_dashboard.exceptions.InvalidDate;
import com.dashboard.doctor_dashboard.exceptions.ReportNotFound;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.exceptions.ValidationsException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
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

    private final static Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();

//    ArrayList<>(Collections.nCopies(12,true))
    static final List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
    List<String> times=Arrays.asList("10:00","10:30","11:00","11:30","12:00","12:30","14:00","14:30","15:00","15:30","16:00","16:30");


    @Autowired
    private ModelMapper mapper;


    public AppointmentServiceImpl() {
    }

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
                            Appointment getAppointmentById = appointmentRepository.getAppointmentById(appointment.getFollowUpAppointmentId());
                            if (appointment.getPatient().getPID() != getAppointmentById.getPatient().getPID()) {
                                throw new ResourceNotFoundException("pat", "id", appointment.getFollowUpAppointmentId());
                            }
                            appointmentRepository.changeFollowUpStatus(appointment.getFollowUpAppointmentId());
                            appointment.setIsBookedAgain(null);
                        }
//
//                      appointment.setBookedAgain(true);
                        else {
                            throw new ResourceNotFoundException("appointment", "id", appointment.getFollowUpAppointmentId());
                        }
                    }
                    List<Boolean> c = new ArrayList<>(checkSlots(appointment.getDateOfAppointment(), appointment.getDoctorDetails().getId()));
                    String time=appointment.getAppointmentTime().toString();
                    int index = times.indexOf(time);
                        if(index==-1)
                            throw new InvalidDate(appointment.getAppointmentTime().toString(),"Invalid time");
                    if(c.get(index)) {
                            c.set(index, false);
                    }else {
                        throw new InvalidDate(appointment.getAppointmentTime().toString(),"appointment is already booked for this time, please refresh.");
                    }
                    slots.get(appointment.getDoctorDetails().getId()).put(appointment.getDateOfAppointment(), c);
                    appointment.setStatus("To be attended");

                    appointmentRepository.save(appointment);
                    m.put("appointId",appointment.getAppointId().toString());
                    m.put("message","Appointment Successfully created..");
                    sendEmailToUser(appointment);
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,m),HttpStatus.OK);
                }
                throw new InvalidDate(appDate.toString(),"appointment cannot be booked on this date");
            }else if(patientId!=null){
                throw new ResourceNotFoundException("Patient", "id", loginId);
            } else if (doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId()) == null) {
                throw new ResourceNotFoundException("Doctor", "id", loginId);
            }

        }
        throw new ResourceNotFoundException("Patient", "id", loginId);
    }

    private void checkSanityOfAppointment(Appointment appointment){

        long patientId=appointment.getPatient().getPID();
        long doctorId=appointment.getDoctorDetails().getId();

        LoginDetails patientLoginDetails= loginRepo.findById(patientId).get();
        LoginDetails doctorLoginDetails= loginRepo.findById(doctorId).get();
//        a appointment.getPatientEmail()
//        assert patientLoginDetails.getEmailId()==appointment.getPatientEmail();
        if(!patientLoginDetails.getEmailId().equals(appointment.getPatientEmail())){
            throw new ValidationsException(new ArrayList<>(List.of("invalid patient email")));
        }if(!patientLoginDetails.getName().equals(appointment.getPatientName())){
            throw new ValidationsException(new ArrayList<>(List.of("invalid patient name")));
        }if(!doctorLoginDetails.getName().equals(appointment.getDoctorName())){
            throw new ValidationsException(new ArrayList<>(List.of("invalid doctor name")));
        }
    }
    public Map<Long,Map<LocalDate,List<Boolean>>> returnMap(){
        return slots;
    }
    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long loginId) {

        GenericMessage genericMessage = new GenericMessage();
        List<PatientAppointmentListDto> today = new ArrayList<>();
        Map<String,List<PatientAppointmentListDto>> m = new HashMap<>();

        Long patientId=patientRepository.getId(loginId);
        System.out.println(patientId);
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
      throw new ResourceNotFoundException("Patient", "id", loginId);

}

    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByDoctorId(Long loginId) {

        GenericMessage genericMessage = new GenericMessage();
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
        throw new ResourceNotFoundException("Doctor", "id", doctorId);

    }




//    @Override
//    public ResponseEntity<GenericMessage> getPastAppointmentByDoctorId(Long doctorId) {
//        GenericMessage genericMessage = new GenericMessage();
//        if(doctorRepository.isIdAvailable(doctorId) != null) {
//            List<DoctorAppointmentListDto> past = mapToAppointDoctorList(appointmentRepository.pastDoctorAppointment(doctorId));
//            genericMessage.setData(past);
//            genericMessage.setStatus(Constants.SUCCESS);
//
//            return new ResponseEntity<>(genericMessage,HttpStatus.OK);
//        }
//        throw new ResourceNotFoundException("Doctor", "id", doctorId);
//
//    }
//
//
//    @Override
//    public ResponseEntity<GenericMessage> getTodayAppointmentByDoctorId(Long doctorId) {
//        if(doctorRepository.isIdAvailable(doctorId) != null) {
//            GenericMessage genericMessage = new GenericMessage();
//            List<DoctorAppointmentListDto> today1 = mapToAppointDoctorList(appointmentRepository.todayDoctorAppointment1(doctorId));
//            List<DoctorAppointmentListDto> today2 = mapToAppointDoctorList(appointmentRepository.todayDoctorAppointment2(doctorId));
//
//            List<DoctorAppointmentListDto> today = new ArrayList<>();
//            today.addAll(today1);
//            today.addAll(today2);
//            genericMessage.setData(today);
//            genericMessage.setStatus(Constants.SUCCESS);
//
//
//            return new ResponseEntity<>(genericMessage, HttpStatus.OK);
//        }
//        throw new ResourceNotFoundException("Doctor", "id", doctorId);
//
//    }
//
//
//
//
//    @Override
//    public ResponseEntity<GenericMessage> getUpcomingAppointmentByDoctorId(Long doctorId) {
//        GenericMessage genericMessage = new GenericMessage();
//        if(doctorRepository.isIdAvailable(doctorId) != null) {
//            List<DoctorAppointmentListDto> upcoming = mapToAppointDoctorList(appointmentRepository.upcomingDoctorAppointment(doctorId));
//
//            genericMessage.setData(upcoming);
//            genericMessage.setStatus(Constants.SUCCESS);
//
//
//            return new ResponseEntity<>(genericMessage, HttpStatus.OK);
//        }
//        throw new ResourceNotFoundException("Doctor", "id", doctorId);
//    }


    @Override
    public ResponseEntity<GenericMessage> getFollowDetails(Long appointId) {
        if(appointmentRepository.getId(appointId) != null && appointId == appointmentRepository.getId(appointId)){
            System.out.println(mapper.map(appointmentRepository.getFollowUpData(appointId),FollowUpDto.class));
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,mapper.map(appointmentRepository.getFollowUpData(appointId),FollowUpDto.class)),HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Appointment", "id", appointId);
    }


    @Override
    public PatientProfileDto getAppointmentById(Long appointId) {
        Appointment appointment = appointmentRepository.getAppointmentById(appointId);
        System.out.println(appointment);
        System.out.println( mapper.map(appointment,PatientProfileDto.class));
        return mapper.map(appointment,PatientProfileDto.class);
    }

    @Override
    public ResponseEntity<GenericMessage> weeklyDoctorCountChart(Long doctorId) {
        int lengthOfMonth = LocalDate.now().lengthOfMonth();

        ArrayList<String> newList = new ArrayList<>();
        var year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        var month= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        if(Integer.parseInt(month)<10){
            month="0"+month;
        }
        System.out.println(month);
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


        ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByDoctorId(doctorId);
        System.out.println(dateList);
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


    @Override
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(Long patientId) {
        int lengthOfMonth = LocalDate.now().lengthOfMonth();
        Long id = patientRepository.getId(patientId);

        ArrayList<String> newList = new ArrayList<>();
        var year= String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        var month= String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        if(Integer.parseInt(month)<10){
            month="0"+month;
        }
        System.out.println(month);
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


        ArrayList<java.sql.Date> dateList = appointmentRepository.getAllDatesByPatientId(id);
        System.out.println(dateList);
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


    @Override
    public ResponseEntity<GenericMessage> recentAppointment(Long doctorId) {

        List<Appointment> appointments = appointmentRepository.recentAppointment(doctorId);
        List<DoctorAppointmentListDto> list =  appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,list),HttpStatus.OK);
    }



    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointment(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentRepository.totalNoOfAppointment(doctorId)),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> todayAppointments(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentRepository.todayAppointments(doctorId)),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentRepository.totalNoOfAppointmentAddedThisWeek(doctorId)),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<GenericMessage> patientCategoryGraph(Long loginId) {
        Long patientId = patientRepository.getId(loginId);
        if(patientId != null) {
            return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS, appointmentRepository.patientCategoryGraph(patientId)), HttpStatus.OK);
        }
        throw new ResourceNotFoundException("Patient", "id", loginId);

    }


    private Map<Long,Map<LocalDate,List<Boolean>>> checkSlotsAvail(LocalDate date,Long doctorId){
        Map<LocalDate,List<Boolean>> dateAndTime=new HashMap<>();

    List<Boolean> docTimesSlots=new ArrayList<>(Collections.nCopies(12,true));
        List<LocalTime> doctorBookedSlots;
        List<Time> dates=appointmentRepository.getTimesByIdAndDate(date,doctorId);
        doctorBookedSlots=dates.stream().map((n)->n.toLocalTime()).collect(Collectors.toList());
        if(doctorBookedSlots.isEmpty()==false){
            for (int i = 0; i < doctorBookedSlots.size(); i++) {
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
        System.out.println(slots);
        if(doctorRepository.isIdAvailable(doctorId)!=null) {
            if (slots.get(doctorId) != null) {
                if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(8))) {

                    if (slots.get(doctorId).get(date) != null) {
                        List<Boolean> returnList=new ArrayList<>(slots.get(doctorId).get(date));
                        return slots.get(doctorId).get(date);
                    } else {
                        return checkSlotsAvail(date, doctorId).get(doctorId).get(date);
                    }
                }else {
                    throw new InvalidDate(date.toString(),"select dates from specified range");
                }
            }
            else{
                if (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusDays(8))) {
                    return checkSlotsAvail(date, doctorId).get(doctorId).get(date);
                }
                else {
                    throw new InvalidDate(date.toString(),"select dates from specified range");
                }
            }
        }
        throw new ResourceNotFoundException("Doctor","id",doctorId);

    }

    List<PatientAppointmentListDto> mapToAppointList(List<Appointment> appointments){
        List<PatientAppointmentListDto> list = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());

        return list;
    }

    List<DoctorAppointmentListDto> mapToAppointDoctorList(List<Appointment> appointments){
        List<DoctorAppointmentListDto> list = appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());

        return list;
    }

    private DoctorAppointmentListDto mapToDto(Appointment appointment) {
        return mapper.map(appointment, DoctorAppointmentListDto.class);
    }

    private PatientAppointmentListDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientAppointmentListDto.class);
    }

    public void sendEmailToUser(Appointment appointment) throws JSONException, MessagingException, UnsupportedEncodingException {
        String doctorEmail = loginRepo.email(appointment.getDoctorDetails().getId());
        System.out.println(doctorEmail);
        String toEmail = appointment.getPatientEmail();
        String fromEmail = "mecareapplication@gmail.com";
        String senderName = "meCare Team";
        String subject = "Appointment Confirmed";

        String content = "<head><style>table, th, td {border: 1px solid black;border-collapse: collapse;padding: 15px;margin-top: auto; }"
                + "</style></head>"
                + "<div style=\"background-color: white; color:black  \">\n"
                + " <p style=\"text-align: left; font-size:15px ;\">Hi [[name]],</p>\n"
                + " <p style =\"text-align:left; font-size:15px ;line-height: 0.8\n"
                + " font-family: 'Arial' \n" + " ;\n"
                + " \"\n" + " >\n"
                + "Your appointment has been booked. Check the details given below.</p>"

                + "<table><tr><th>Doctor Name</th><th>Doctor Email</th><th>Speciality</th><th>Date of Appointment</th><th>Appointment Time</th></tr><tr><td>Dr.[[doctorName]]</td><td>[[doctorEmail]]</td><td>[[speciality]]</td><td>[[dateOfAppointment]]</td><td>[[appointmentTime]]</td></tr></table>"

                + " <p style=\" text-align: left ;font-size:13px \">\n"
                + " For further queries, please mail to:\n" + " <span style=\"color: #FFFFF; \"\n"
                + " >mecareapplication@gmail.com</span\n" + " >\n" + " </p>\n"
                + " <p style=\" text-align: left;font-size:13px;line-height: 0.8\">\n"
                + " Thanks & Regards, </p>\n"
                + " <p style=\"font-size: 13px; text-align: left;line-height: 0.8\">meCare team</span\n"
                + " </div>";

        content = content.replace("[[name]]", appointment.getPatientName());
        content = content.replace("[[doctorName]]", appointment.getDoctorName());
        content = content.replace("[[doctorEmail]]", doctorEmail);
        content = content.replace("[[speciality]]", appointment.getCategory());
        content = content.replace("[[dateOfAppointment]]", formatDate(appointment.getDateOfAppointment().toString()));
        content = content.replace("[[appointmentTime]]", appointment.getAppointmentTime().toString());
        JSONObject obj = new JSONObject();
        obj.put("fromEmail", fromEmail);
        obj.put("toEmail", toEmail);
        obj.put("senderName", senderName);
        obj.put("subject", subject);
        obj.put("content", content);
        sendMailer(obj);
    }


    private void sendMailer(JSONObject obj) throws MessagingException, JSONException, UnsupportedEncodingException {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(obj.get("fromEmail").toString(), obj.get("senderName").toString());
            helper.setTo(obj.get("toEmail").toString());
            helper.setText(obj.get("content").toString(), true);
            helper.setSubject(obj.get("subject").toString());
            System.out.println("I am printing"+message.getSubject());
            mailSender.send(message);
        }catch (Exception e)
        {
            throw new ReportNotFound(e.getMessage());
        }

    }

    String formatDate(String Date1){
        String[] newArray = Date1.split("-",5);
        String newDate = newArray[2]+"-"+newArray[1]+"-"+newArray[0];
        return newDate;
    }



}
