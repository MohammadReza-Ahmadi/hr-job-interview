package com.interview.backend.service;

import com.interview.backend.model.Interview;

import java.util.List;

public interface InterviewService {

    Interview save(Interview interview);

    List<Interview> findAll();

    Interview findInterviewById(Long id);

    List<Interview> searchInterviews(String name, String surname, String email, String mobile);

    void deleteInterviewById(Long id);
}
