package com.interview.frontend.service.impl;

import com.interview.frontend.model.InterviewDto;
import com.interview.frontend.repository.InterviewRepository;
import com.interview.frontend.service.InterviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;

    @Override
    public List<InterviewDto> getInterviews() {
        ResponseEntity<List<InterviewDto>> responseEntity = interviewRepository.getInterviews();
        if (isOk(responseEntity)) {
            return responseEntity.getBody();
        }
        return null;
    }

    @Override
    public void createInterview(InterviewDto request) {
        interviewRepository.createInterview(request);
    }

    @Override
    public InterviewDto getInterviewById(Long id) {
        ResponseEntity<InterviewDto> responseEntity = interviewRepository.getInterviewById(id);
        if (isOk(responseEntity)) {
            return responseEntity.getBody();
        }
        return null;
    }

    @Override
    public List<InterviewDto> searchInterviews(String name, String surname, String email, String mobile) {
        ResponseEntity<List<InterviewDto>> responseEntity = interviewRepository.searchInterviews(name, surname, email, mobile);
        if (isOk(responseEntity)) {
            return responseEntity.getBody();
        }
        return null;
    }

    @Override
    public void deleteInterviewById(Long id) {
        interviewRepository.deleteInterviewById(id);
    }

    private boolean isOk(ResponseEntity responseEntity) {
        return responseEntity.getStatusCode().equals(HttpStatus.OK);
    }
}
