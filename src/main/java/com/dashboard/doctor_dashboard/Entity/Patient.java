package com.dashboard.doctor_dashboard.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "patients"
)
public class Patient {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pID;

    @NotEmpty
    @Size(min = 3,max = 100,message = "Name field should contain Atleast 3 character")
    @Pattern(regexp = "^[\\p{L} .'-]+$",message = "full name should only contain characters")
    private String fullName;


    @Email(message = "Email is not valid",
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotEmpty
    private String emailId;


    @NotNull
    @PastOrPresent
    private LocalDate lastVisitedDate;

    @NotEmpty
    @Pattern(regexp = "^(Male|Female|Others)",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Enter Correct Gender!!")
    private String gender;


    @Positive(message = "age can't be null or less than equal to 0")
    private int age;

    @NotEmpty
    @Pattern(regexp = "^(General|Gastrology|Neurology|Orthology|Cardiology)",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message ="Category Should be Amongst these choices:" +
                    "General,Gastrology,Neurology,Orthology,Cardiology")
    private String category;

    @NotEmpty
    @Size(min = 10,max = 10,message = "phone Number should Contains only 10 digits")
    @Pattern(regexp = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$")
    private String mobileNo;


    private String status;
    private boolean isChanged;
    private String message;

    @JsonManagedReference
    @OneToOne(mappedBy = "patient",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Attributes attributes;


    @ManyToOne()
    @JsonBackReference
    @JoinColumn(name = "doctor_id")
    private DoctorDetails doctorDetails;


}
