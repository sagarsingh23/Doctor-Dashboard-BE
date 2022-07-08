package com.dashboard.doctor_dashboard.entities.login_entity;


import com.dashboard.doctor_dashboard.entities.model.DoctorDetails;
import com.dashboard.doctor_dashboard.entities.model.Patient;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "login_details", uniqueConstraints = @UniqueConstraint(columnNames = {"emailId"}))
public class LoginDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "emailId", nullable = false, unique = true)
    private String emailId;
    @Column(name = "domain", nullable = false)
    private String domain;
    @Column(name = "profile_pic",nullable = false,unique = true)
    private String profilePic;
    @Column(name="role",nullable = false)
    private String role;

    @CreationTimestamp
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    @OneToOne(mappedBy = "loginDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DoctorDetails doctorDetails;


    @JsonManagedReference
    @OneToOne(mappedBy = "loginDetails",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Patient patient;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


    @Override
    public String toString() {
        return "LoginDetails{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", emailId='" + emailId + '\'' +
                ", domain='" + domain + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", doctorDetails=" + doctorDetails +
                ", patient=" + patient +
                '}';
    }
}
