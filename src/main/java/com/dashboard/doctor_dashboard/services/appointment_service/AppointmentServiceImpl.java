package com.dashboard.doctor_dashboard.services.appointment_service;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.entities.dtos.*;
import com.dashboard.doctor_dashboard.exceptions.InvalidDate;
import com.dashboard.doctor_dashboard.exceptions.ResourceNotFoundException;
import com.dashboard.doctor_dashboard.jwt.security.JwtTokenProvider;
import com.dashboard.doctor_dashboard.repository.AppointmentRepository;
import com.dashboard.doctor_dashboard.repository.DoctorRepository;
import com.dashboard.doctor_dashboard.repository.LoginRepo;
import com.dashboard.doctor_dashboard.repository.PatientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Array;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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

    private final static Map<Long,Map<LocalDate,List<Boolean>>> slots=new HashMap<>();

//    ArrayList<>(Collections.nCopies(12,true))
    static final List<Boolean> timesSlots=List.of(true,true,true,true,true,true,true,true,true,true,true,true);
    List<String> times=Arrays.asList("10:00","10:30","11:00","11:30","12:00","12:30","14:00","14:30","15:00","15:30","16:00","16:30");


    @Autowired
    private ModelMapper mapper;
    @Override
    public ResponseEntity<GenericMessage>  addAppointment(Appointment appointment, HttpServletRequest request) {

        System.out.println(slots);
        Long loginId=jwtTokenProvider.getIdFromToken(request);
        if (loginRepo.isIdAvailable(loginId) != null) {
            Long patientId=patientRepository.getId(appointment.getPatient().getPID());
            if ( patientId!= null && doctorRepository.isIdAvailable(appointment.getDoctorDetails().getId()) != null) {
                appointment.getPatient().setPID(patientId);
                LocalDate appDate=appointment.getDateOfAppointment();
                if(appDate.isAfter(LocalDate.now())&&appDate.isBefore(LocalDate.now().plusDays(8))) {
                    List<Boolean> c = new ArrayList<>(checkSlots(appointment.getDateOfAppointment(), appointment.getDoctorDetails().getId()));
                    String time=appointment.getAppointmentTime().toString();
                    int index = times.indexOf(time);
                        System.out.println(time+","+index);
                        if(index==-1)
                            throw new InvalidDate(appointment.getAppointmentTime().toString(),"Invalid time");

                    if(c.get(index)) {
                            c.set(index, false);
                            System.out.println("c"+c);

                    }else {
                        throw new InvalidDate(appointment.getAppointmentTime().toString(),"appointment is already booked for this time, please refresh.");
                    }
                    slots.get(appointment.getDoctorDetails().getId()).put(appointment.getDateOfAppointment(), c);
                    System.out.println("book app"+slots);
                    appointment.setStatus("To be attended");
                    appointmentRepository.save(appointment);
                    return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,"appointment created successfully"),HttpStatus.OK);
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
    public Map<Long,Map<LocalDate,List<Boolean>>> returnMap(){
        return slots;
    }
    @Override
    public ResponseEntity<GenericMessage> getAllAppointmentByPatientId(Long loginId) {

        System.out.println(LocalTime.now());
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

    List<PatientAppointmentListDto> mapToAppointList(List<Appointment> appointments){
        List<PatientAppointmentListDto> list = appointments.stream()
                .map(this::mapToDto2).collect(Collectors.toList());

        return list;
    }

    @Override
    public List<DoctorAppointmentListDto> getAllAppointmentByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.getAllAppointmentByDoctorId(doctorId);
        List<DoctorAppointmentListDto> list = appointments.stream()
                .map(this::mapToDto).collect(Collectors.toList());

        return list;
    }

    @Override
    public PatientProfileDto getAppointmentById(Long appointId) {
        Appointment appointment = appointmentRepository.getAppointmentById(appointId);
        return mapper.map(appointment,PatientProfileDto.class);
    }

    @Override
    public ResponseEntity<GenericMessage> weeklyPatientCountChart(Long doctorId) {
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
//                System.out.println(date.toLocalDate());
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

//    private DoctorAppointmentListDto mapToDto(Appointment appointment) {
//        return mapper.map(appointment, DoctorAppointmentListDto.class);
//    }
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
    public ResponseEntity<GenericMessage> totalNoOfAppointmentAddedThisWeek(Long doctorId) {
        return new ResponseEntity<>(new GenericMessage(Constants.SUCCESS,appointmentRepository.totalNoOfAppointmentAddedThisWeek(doctorId)),HttpStatus.OK);
    }


    private Map<Long,Map<LocalDate,List<Boolean>>> checkSlotsAvail(LocalDate date,Long doctorId){
//        System.out.println(date);
        Map<LocalDate,List<Boolean>> dateAndTime=new HashMap<>();
         List<Boolean> docTimesSlots=new ArrayList<>(Collections.nCopies(12,true));
        System.out.println("timeslots"+timesSlots);
        List<LocalTime> doctorBookedSlots;
        List<Time> dates=appointmentRepository.getTimesByIdAndDate(date,doctorId);
        doctorBookedSlots=dates.stream().map((n)->n.toLocalTime()).collect(Collectors.toList());
        System.out.println(doctorBookedSlots.isEmpty());
        if(doctorBookedSlots.isEmpty()==false){
            for (int i = 0; i < doctorBookedSlots.size(); i++) {
                docTimesSlots.set(times.indexOf(doctorBookedSlots.get(i).toString()),false);
            }
            System.out.println("timeslots"+docTimesSlots);
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
        System.out.println("timeslots"+timesSlots);
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
                        System.out.println("yes"+slots.get(doctorId).get(date));
                        List<Boolean> returnList=new ArrayList<>(slots.get(doctorId).get(date));
//                        return new ArrayList<>(slots.get(doctorId).get(date));
//                        return slots.get(doctorId).get(date);
                        return slots.get(doctorId).get(date);
                    } else {
                        System.out.println("no"+slots.get(doctorId).get(date));

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



    private DoctorAppointmentListDto mapToDto(Appointment appointment) {
        return mapper.map(appointment, DoctorAppointmentListDto.class);
    }

    private PatientAppointmentListDto mapToDto2(Appointment appointment) {
        return mapper.map(appointment, PatientAppointmentListDto.class);
    }


}
