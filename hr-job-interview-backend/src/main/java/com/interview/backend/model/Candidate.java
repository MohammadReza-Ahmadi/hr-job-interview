package com.interview.backend.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String surname;

    @Pattern(regexp="(^$|[+][0-9]{12})", message = "Please provide a valid mobile number")
    String mobile;

    @Email(message="Please provide a valid email address")
    String email;

    @OneToMany(targetEntity=Interview.class, mappedBy="candidate")
    Set<Interview> interviews;
}
