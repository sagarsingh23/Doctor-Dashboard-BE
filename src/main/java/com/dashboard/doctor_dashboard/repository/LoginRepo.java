package com.dashboard.doctor_dashboard.repository;

import com.dashboard.doctor_dashboard.entities.login_entity.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<LoginDetails, Long> {
    LoginDetails findByEmailId(String email);

    @Query(value = "select id from login_details l where l.email_id =:email", nativeQuery = true)
    int getId(String email);

    @Query(value = "select email_id from login_details where id =:doctorId",nativeQuery = true)
    String email(Long doctorId);

    @Query(value = "select id from login_details d where d.id=:id", nativeQuery = true)
    Long isIdAvailable(Long id);
    @Query(value = "select role from login_details d where d.id=:id", nativeQuery = true)
    String  getRoleById(Long id);

    @Query(value = "select profile_pic from login_details d where d.id=:id", nativeQuery = true)
    String getProfilePic(Long id);
    Optional<LoginDetails> findByNameOrEmailId(String usernameOrEmail, String username);
}
