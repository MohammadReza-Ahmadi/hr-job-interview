package com.interview.backend.controller.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InterviewDto {
    Long id;
    String name;
    String surname;

    @Pattern(regexp="(^$|[+][0-9]{12})", message = "Invalid mobile number!")
    String mobile;

    @Email(message = "Invalid email address!")
    String email;

    LocalDateTime interviewDateTime;
    Integer salaryExpectation;
    String commentForCandidate;
}
