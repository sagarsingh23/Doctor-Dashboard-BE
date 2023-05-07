package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.Appointment;
import com.dashboard.doctor_dashboard.dtos.AppointmentViewDto;
import com.dashboard.doctor_dashboard.dtos.NotificationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface AppointmentRepository extends PagingAndSortingRepository<Appointment, Long> {
    @Query(value = "select appoint_id from appointments where appoint_id=:appointmentId", nativeQuery = true)
    Long getId(Long appointmentId);

    @Query(value = "select * from appointments where patient_id = :patientId",nativeQuery = true)
    List<Appointment> getAllAppointmentByPatientId(Long patientId);


    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment < curdate()",nativeQuery = true)
    Page<Appointment> pastAppointment(Long patientId,Pageable pageable);

    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment = curdate() and appointment_time >= time(now())",nativeQuery = true)
    Page<Appointment> todayAppointment1(Long patientId,Pageable pageable);

    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment = curdate() and appointment_time < time(now())",nativeQuery = true)
    Page<Appointment> todayAppointment2(Long patientId,Pageable pageable);

    @Query(value = "select * from appointments where patient_id = :patientId and date_of_appointment > curdate()",nativeQuery = true)
    Page<Appointment> upcomingAppointment(Long patientId,Pageable pageable);



    @Query(value = "select * from appointments where doctor_id = :doctorId and date_of_appointment < curdate()",nativeQuery = true)
    Page<Appointment> pastDoctorAppointment(Long doctorId,Pageable pageable);

    @Query(value = "select * from appointments where doctor_id = :doctorId and date_of_appointment = curdate() and appointment_time >= time(now()) and status='Vitals updated'",nativeQuery = true)
    Page<Appointment> todayDoctorAppointment1(Long doctorId,Pageable pageable);

    @Query(value = "select * from appointments where doctor_id = :doctorId and date_of_appointment = curdate() and (status='Completed' or status='Follow Up') ",nativeQuery = true)
    Page<Appointment> todayDoctorAppointment2(Long doctorId,Pageable pageable);

    @Query(value = "select * from appointments where doctor_id = :doctorId and date_of_appointment > curdate()",nativeQuery = true)
    Page<Appointment> upcomingDoctorAppointment(Long doctorId,Pageable pageable);


    @Query(value = "select count(*) from appointments where doctor_id = :doctorId and date_of_appointment = curdate()",nativeQuery = true)
    int todayAppointments(Long doctorId);





    @Query(value = "update appointments set status =:status, is_read=1 where appoint_id=:appointId", nativeQuery = true)
    @Modifying
    @Transactional
    void changeAppointmentStatus(Long appointId, String status);

    @Query(value = "update appointments set is_booked_again =true where appoint_id=:appointId", nativeQuery = true)
    @Modifying
    @Transactional
    void changeFollowUpStatus(Long appointId);


    @Query(value = "select appointment_time from appointments where doctor_id=:doctorId and date_of_appointment=:date ",nativeQuery = true)
    List<Time>getTimesByIdAndDate(LocalDate date, Long doctorId);

    @Query(value = "select * from appointments where doctor_id = :doctorId",nativeQuery = true)
    List<Appointment> getAllAppointmentByDoctorId(Long doctorId);

    @Query(value = "select * from appointments where doctor_id = :doctorId and date_of_appointment = curdate() and appointment_time >= time(now()) and status='Vitals updated' limit 3", nativeQuery = true)
    List<Appointment> recentAppointment(Long doctorId);

    @Query(value = "select * from appointments where appoint_id = :appointId",nativeQuery = true)
    Appointment getAppointmentById(Long appointId);

    @Query(value = "Select date_of_appointment from appointments where doctor_id =:doctorId ", nativeQuery = true)
    ArrayList<Date> getAllDatesByDoctorId(@Param(value = "doctorId") Long doctorId);

    @Query(value = "Select date_of_appointment from appointments where patient_id =:patientId", nativeQuery = true)
    ArrayList<Date> getAllDatesByPatientId(@Param(value = "patientId") Long patientId);

    @Query(value = "Select COUNT(appoint_id) from appointments where doctor_id =:doctorId", nativeQuery = true)
    int totalNoOfAppointment(@Param(value = "doctorId") Long doctorId);

    @Query(value = "select count(appoint_id) from appointments where doctor_id=:doctorId and week(timestamp)=week(now())", nativeQuery = true)
    int totalNoOfAppointmentAddedThisWeek(@Param(value = "doctorId") Long doctorId);

    @Query(value = "select * from appointments where doctor_id = :doctorId and date_of_appointment = curdate() and status='To be attended'",nativeQuery = true)
    Page<Appointment> receptionistDoctorAppointment(Long doctorId,Pageable pageable);


    @Query(value = "select new com.dashboard.doctor_dashboard.dtos.AppointmentViewDto(appo.doctorName,appo.category,appo.dateOfAppointment,appo.appointmentTime,appo.status,pati.bloodGroup,dd.age,dd.gender) from Appointment appo join Patient pati on  pati.deleted = false and appo.appointId=:appointmentId and appo.patient.pID=:patientId and appo.patient.pID=pati.pID join DoctorDetails dd on appo.doctorDetails.id=dd.id")
    AppointmentViewDto getBasicAppointmentDetails(long appointmentId,long patientId);

    @Query(value = "update appointments set status=:status where appoint_id=:appointmentId ", nativeQuery = true)
    @Modifying
    @Transactional
    void setStatus(String status,Long appointmentId);



    @Query(value = "select category,count(category) from appointments where  patient_id =:patientId group by category", nativeQuery = true)
    ArrayList<String> patientCategoryGraph(@Param(value = "patientId") Long patientId);


    @Query(value = "select * from appointments where appoint_id=:appointId",nativeQuery = true)
    Appointment getFollowUpData(Long appointId);

    @Query(value = "select doctor_id from appointments where appoint_id=:appointmentId ", nativeQuery = true)
    Long getDoctorId(Long appointmentId);

    @Query(value = "select gender from doctor_details where deleted = false and id=:doctorId",nativeQuery = true)
    String getGenderById(Long doctorId);
    @Query(value = "select email_id from login_details where deleted = false and id=:loginId",nativeQuery = true)
    String getEmailById(Long loginId);

    @Query(value = "select new com.dashboard.doctor_dashboard.dtos.NotificationDto(appointId,doctorName) from Appointment a where a.patient.pID=:patientId and isRead=true")
    List<NotificationDto> getNotifications(Long patientId);

    @Query(value = "select * from appointments where  date_of_appointment = curdate() and status ='To be attended' order by appointment_time",nativeQuery = true)
    Page<Appointment> todayAllAppointmentForClinicStaff1(Pageable pageable);

    @Query(value = "select * from appointments where  date_of_appointment = curdate() and status !='To be attended' order by appointment_time",nativeQuery = true)
    Page<Appointment> todayAllAppointmentForClinicStaff2(Pageable pageable);

    @Query(value = "select status from appointments where  appoint_id =:appointId",nativeQuery = true)
    String checkStatus(Long appointId);
}
