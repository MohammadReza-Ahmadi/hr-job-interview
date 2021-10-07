package com.interview.frontend.service;

import com.interview.frontend.model.InterviewDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface InterviewService {

    List<InterviewDto> getInterviews();

    void createInterview(InterviewDto request);

    InterviewDto getInterviewById(@PathVariable Long id);

    List<InterviewDto> searchInterviews(String name, String surname, String email, String mobile);

    void deleteInterviewById(@PathVariable Long id);
}
