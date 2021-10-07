package com.interview.backend.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="CANDIDATE_ID", referencedColumnName="ID")
    Candidate candidate;

    LocalDateTime interviewDatetime;

    Integer salaryExpectation;

    String commentForCandidate;
}
