package com.dashboard.doctor_dashboard.Repository;

import com.dashboard.doctor_dashboard.Entity.DoctorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DoctorRepository extends JpaRepository<DoctorDetails,Long> {
    public List<DoctorDetails> findByFirstName(String name);
    public List<DoctorDetails> findByLastName(String name);

    public List<DoctorDetails>  findByAge(short age);
    public DoctorDetails findByEmail(String email);
    @Query(value = "select speciality from doctor_details d where d.id=:id",nativeQuery = true)
    public String findBySpeciality(long id);

    @Query(value = "select email from doctor_details d where d.id=:id",nativeQuery = true)
    public String findEmail(long id);

}
