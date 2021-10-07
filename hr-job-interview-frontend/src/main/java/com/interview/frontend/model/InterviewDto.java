package com.interview.frontend.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Data
public class InterviewDto {
    Long id;

    @NonNull
    String name;

    @NonNull
    String surname;

    @NonNull
    String mobile;

    @NonNull
    String email;

    @NonNull
    LocalDateTime interviewDateTime;

    @NonNull
    Integer salaryExpectation;

    @NonNull
    String commentForCandidate;
}
